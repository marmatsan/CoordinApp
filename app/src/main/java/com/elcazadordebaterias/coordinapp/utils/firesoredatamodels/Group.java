package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.jetbrains.annotations.NotNull;

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

    // Identifier 1
    public static final int WITH_TEACHER = 0;
    public static final int ONLY_STUDENTS = 1;

    // Identifier 2
    // If a group is of type WITH_TEACHER, it can be groupal or individual (just the teacher and the student)
    public static final int ISGROUPAL = 0;
    public static final int ISINDIVIDUAL = 1;

    private String parentID; // ID for the group. If ISGROUPAL, it is the ID of the parent document containing the "Groups collection" with the chats.
                             // If ISINDIVIDUAL, it is the ID of the own document

    private String name; // The name of the group
    private String coordinatorId; // The id of the teacher that created the group
    private String coordinatorName; // The name of the teacher that created the group
    private String courseName;     // The name of this group
    private String subjectName;

    private ArrayList<String> participantsIds;
    private ArrayList<GroupParticipant> participants;

    private String collectionId;

    private int identifier1;
    private int identifier2;

    public Group() {

    }

    public Group(String parentID, String name, String coordinatorId, String coordinatorName, String courseName, String subjectName, ArrayList<String> participantsIds, ArrayList<GroupParticipant> participants, String collectionId, int identifier1, int identifier2) {
        this.parentID = parentID;
        this.name = name;
        this.coordinatorId = coordinatorId;
        this.coordinatorName = coordinatorName;
        this.courseName = courseName;
        this.subjectName = subjectName;
        this.participantsIds = participantsIds;
        this.participants = participants;
        this.collectionId = collectionId;
        this.identifier1 = identifier1;
        this.identifier2 = identifier2;
    }

    public String getParentID() {
        return parentID;
    }

    public String getName() {
        return name;
    }

    public String getCoordinatorId() {
        return coordinatorId;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSubjectName() {
        return subjectName;
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

    public int getIdentifier1() {
        return identifier1;
    }

    public int getIdentifier2() {
        return identifier2;
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

        if (onlyStudentsIDs.size() == 1) { // The group has multiple students. We want to create them in the IndividualGroups collection
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

                    Group studentsAndTeacherGroup = new Group(
                            "",
                            "Grupo " + identifier,
                            teacherInfo.getId(),
                            (String) teacherInfo.get("FullName"),
                            selectedCourse,
                            selectedSubject,
                            studentsAndTeacherIDs,
                            studentsAndTeacherParticipants,
                            groupsCollRef.getId(),
                            Group.WITH_TEACHER,
                            Group.ISINDIVIDUAL
                    );

                    groupsCollRef.add(studentsAndTeacherGroup).addOnSuccessListener(documentReference -> {
                        documentReference.update("parentID", documentReference.getId());
                    });

                });
            });
        } else { // The group has multiple students. We want to create them in the CollectiveGroups collection
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

                    GroupDocument collectiveGroupCollection = new GroupDocument("Grupo " + identifier, studentsAndTeacherIDs);

                    groupsCollRef.add(collectiveGroupCollection).addOnSuccessListener(documentReference -> {
                        Group studentsAndTeacherGroup = new Group(
                                documentReference.getId(),
                                "Grupo " + identifier,
                                teacherInfo.getId(),
                                (String) teacherInfo.get("FullName"),
                                selectedCourse,
                                selectedSubject,
                                studentsAndTeacherIDs,
                                studentsAndTeacherParticipants,
                                groupsCollRef.getId(),
                                Group.WITH_TEACHER,
                                Group.ISGROUPAL
                        );

                        Group onlyStudentsGroup = new Group(
                                documentReference.getId(),
                                "Grupo " + identifier,
                                teacherInfo.getId(),
                                (String) teacherInfo.get("FullName"),
                                selectedCourse,
                                selectedSubject,
                                onlyStudentsIDs,
                                onlyStudentsParticipants,
                                groupsCollRef.getId(),
                                Group.ONLY_STUDENTS,
                                Group.ISGROUPAL
                        );

                        documentReference.collection("Groups").document("StudentsAndTeacherGroup").set(studentsAndTeacherGroup);
                        documentReference.collection("Groups").document("OnlyStudentsGroup").set(onlyStudentsGroup);
                    });
                });
            });
        }
    }
}
