package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that represents the data of the group an user is in FireStore. Used when creating a new group,
 * accepted by the teacher.
 *
 * @author Martín Mateos Sánchez
 */
public class Group {

    private String name; // The name of the group
    private String courseName;     // The name of this group
    private String subjectName;

    private boolean hasTeacher;

    private ArrayList<String> participantsIds;
    private ArrayList<GroupParticipant> participants;

    private String collectionId;

    public Group() {

    }

    public Group(String name, String courseName, String subjectName, boolean hasTeacher, ArrayList<String> participantsIds, ArrayList<GroupParticipant> participants, String collectionId) {
        this.name = name;
        this.courseName = courseName;
        this.subjectName = subjectName;
        this.hasTeacher = hasTeacher;
        this.participantsIds = participantsIds;
        this.participants = participants;
        this.collectionId = collectionId;
    }

    public String getName() {
        return name;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setHasTeacher(boolean hasTeacher) {
        this.hasTeacher = hasTeacher;
    }

    public void setParticipantsIds(ArrayList<String> participantsIds) {
        this.participantsIds = participantsIds;
    }

    public void setParticipants(ArrayList<GroupParticipant> participants) {
        this.participants = participants;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public boolean getHasTeacher() {
        return hasTeacher;
    }

    public ArrayList<String> getParticipantsIds() {
        return participantsIds;
    }

    public ArrayList<GroupParticipant> getParticipants() {
        return participants;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public ArrayList<String> getParticipantNames() {
        ArrayList<String> participantsNames = new ArrayList<String>();

        for (GroupParticipant participant : getParticipants()) {
            participantsNames.add(participant.getParticipantFullName());
        }

        return participantsNames;
    }

    public static int getMaxGroupIdentifier(QuerySnapshot queryDocumentSnapshots) {
        ArrayList<String> groupsNames = new ArrayList<String>();

        for (DocumentSnapshot document : queryDocumentSnapshots) {
            GroupDocument groupDocument = document.toObject(GroupDocument.class);
            if (groupDocument.getName() != null) {
                groupsNames.add(groupDocument.getName());
            }
        }

        int maxGroupIdentifier = 0;

        if (!groupsNames.isEmpty()) {
            ArrayList<Integer> numbers = new ArrayList<Integer>();

            for (String identifier : groupsNames) {
                String numberOnly = identifier.replaceAll("[^0-9]", "");
                numbers.add(Integer.parseInt(numberOnly));
            }

            maxGroupIdentifier = Collections.max(numbers);
        }

        return maxGroupIdentifier;
    }

    public static void createGroup(CollectionReference groupsCollRef, String selectedCourse, String selectedSubject, List<String> studentIDs, int identifier) {

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        ArrayList<String> onlyStudentsIDs = new ArrayList<String>(studentIDs);

        fStore.collection("Teachers").document(fAuth.getUid()).get().addOnSuccessListener(teacherInfo -> {
            ArrayList<GroupParticipant> studentsAndTeacherParticipants = new ArrayList<GroupParticipant>();
            ArrayList<String> studentsAndTeacherIDs = new ArrayList<String>(onlyStudentsIDs);

            studentsAndTeacherParticipants.add(new GroupParticipant((String) teacherInfo.get("FullName"), teacherInfo.getId()));
            studentsAndTeacherIDs.add(teacherInfo.getId());

            fStore.collection("Students").whereIn(FieldPath.documentId(), onlyStudentsIDs).get().addOnSuccessListener(queryDocumentSnapshots -> {
                ArrayList<GroupParticipant> onlyStudentsParticipants = new ArrayList<GroupParticipant>();

                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    onlyStudentsParticipants.add(new GroupParticipant((String) document.get("FullName"), document.getId()));
                }

                studentsAndTeacherParticipants.addAll(onlyStudentsParticipants);

                ArrayList<Group> groups = new ArrayList<Group>();

                Group studentsAndTeacherGroup = new Group(
                        "Grupo " + identifier + " - Con profesor",
                        selectedCourse,
                        selectedSubject,
                        true,
                        studentsAndTeacherIDs,
                        studentsAndTeacherParticipants,
                        groupsCollRef.getId()
                );
                groups.add(studentsAndTeacherGroup);

                if (onlyStudentsIDs.size() > 1) {
                    Group onlyStudentsGroup = new Group(
                            "Grupo " + identifier + " - Sólo alumnos",
                            selectedCourse,
                            selectedSubject,
                            false,
                            onlyStudentsIDs,
                            onlyStudentsParticipants,
                            groupsCollRef.getId()
                    );
                    groups.add(onlyStudentsGroup);
                }

                GroupDocument collectiveGroupCollection = new GroupDocument("Grupo " + identifier, studentsAndTeacherIDs, groups);
                groupsCollRef.add(collectiveGroupCollection);
            });
        });
    }
}
