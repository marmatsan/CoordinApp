package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;


import android.content.Context;
import android.widget.Toast;

import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.google.android.gms.tasks.OnSuccessListener;
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

    public static void createGroup(CollectionReference groupsCollRef, String selectedCourse, String selectedSubject, List<String> studentIDs, int identifier, Context context, int userType) {

        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        ArrayList<String> onlyStudentsIDs = new ArrayList<String>(studentIDs);
        String collectionRefID = groupsCollRef.getId();

        fStore
                .collection("Teachers")
                .document(fAuth.getUid())
                .get()
                .addOnSuccessListener(teacherInfo -> {
                    ArrayList<GroupParticipant> studentsAndTeacherParticipants = new ArrayList<GroupParticipant>();
                    ArrayList<String> studentsAndTeacherIDs = new ArrayList<String>(onlyStudentsIDs);

                    studentsAndTeacherParticipants.add(new GroupParticipant((String) teacherInfo.get("FullName"), teacherInfo.getId()));
                    studentsAndTeacherIDs.add(teacherInfo.getId());

                    fStore
                            .collection("Students")
                            .whereIn(FieldPath.documentId(), onlyStudentsIDs)
                            .get()
                            .addOnSuccessListener(studentsDocuments -> {
                                ArrayList<GroupParticipant> onlyStudentsParticipants = new ArrayList<GroupParticipant>();

                                for (QueryDocumentSnapshot document : studentsDocuments) {
                                    onlyStudentsParticipants.add(new GroupParticipant((String) document.get("FullName"), document.getId()));
                                }

                                studentsAndTeacherParticipants.addAll(onlyStudentsParticipants);

                                Group studentsAndTeacherGroup = new Group(
                                        "Grupo " + identifier + " - Con profesor",
                                        selectedCourse,
                                        selectedSubject,
                                        true,
                                        studentsAndTeacherIDs,
                                        studentsAndTeacherParticipants,
                                        groupsCollRef.getId()
                                );

                                if (collectionRefID.equals("CollectiveGroups")) {
                                    ArrayList<Group> groups = new ArrayList<Group>();
                                    groups.add(studentsAndTeacherGroup);

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

                                    GroupDocument newGroupDocument = new GroupDocument("Grupo " + identifier, studentsAndTeacherIDs, groups);

                                    groupsCollRef
                                            .get()
                                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                                boolean groupExists = collectiveGroupExists(queryDocumentSnapshots, newGroupDocument.getAllParticipantsIDs());
                                                if (groupExists) {
                                                    Toast.makeText(context, "Ya existe un grupo igual a: " + newGroupDocument.getName(), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    groupsCollRef.add(newGroupDocument);
                                                }
                                            });

                                } else {
                                    IndividualGroupDocument newGroupDocument = new IndividualGroupDocument("Grupo " + identifier, studentsAndTeacherIDs, studentsAndTeacherGroup);

                                    groupsCollRef
                                            .get()
                                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                                DocumentSnapshot individualGroup = individualGroupExist(queryDocumentSnapshots, newGroupDocument.getAllParticipantsIDs());
                                                if (individualGroup != null) {
                                                    if (userType == UserType.TYPE_TEACHER) {
                                                        individualGroup
                                                                .getReference()
                                                                .update("visible", true);
                                                    }
                                                } else {
                                                    if (userType == UserType.TYPE_TEACHER) {
                                                        newGroupDocument.setVisible(true);
                                                    }
                                                    groupsCollRef.add(newGroupDocument);
                                                }
                                            });

                                }

                            });
                });
    }

    private static boolean collectiveGroupExists(QuerySnapshot queryDocumentSnapshots, ArrayList<String> allParticipantsIDs) {
        boolean groupExists = false;

        for (DocumentSnapshot groupDoc : queryDocumentSnapshots) {
            GroupDocument currentGroupDocument = groupDoc.toObject(GroupDocument.class);
            if (currentGroupDocument.getAllParticipantsIDs().equals(allParticipantsIDs)) {
                groupExists = true;
                break;
            }
        }

        return groupExists;
    }

    private static DocumentSnapshot individualGroupExist(QuerySnapshot queryDocumentSnapshots, ArrayList<String> allParticipantsIDs) {
        DocumentSnapshot individualGroup = null;

        for (DocumentSnapshot groupDoc : queryDocumentSnapshots) {
            IndividualGroupDocument currentGroupDocument = groupDoc.toObject(IndividualGroupDocument.class);
            if (currentGroupDocument.getAllParticipantsIDs().equals(allParticipantsIDs)) {
                individualGroup = groupDoc;
                break;
            }
        }

        return individualGroup;
    }

}
