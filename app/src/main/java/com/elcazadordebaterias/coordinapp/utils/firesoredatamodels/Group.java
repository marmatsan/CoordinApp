package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    private String name; // The name of the group
    private String coordinatorId; // The id of the teacher that created the group
    private String coordinatorName; // The name of the teacher that created the group
    private String courseName;     // The name of this group
    private String subjectName;

    private ArrayList<String> participantsIds;
    private ArrayList<GroupParticipant> participants;

    private String collectionId;

    public Group() {

    }

    public Group(String name, String coordinatorId, String coordinatorName, String courseName, String subjectName, ArrayList<String> participantsIds, ArrayList<GroupParticipant> participants, String collectionId) {
        this.name = name;
        this.coordinatorId = coordinatorId;
        this.coordinatorName = coordinatorName;
        this.courseName = courseName;
        this.subjectName = subjectName;
        this.participantsIds = participantsIds;
        this.participants = participants;
        this.collectionId = collectionId;
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

    public static int getMaxGroupIdentifier(QuerySnapshot queryDocumentSnapshots) {
        ArrayList<String> groupsNames = new ArrayList<String>();

        for (DocumentSnapshot document : queryDocumentSnapshots) {
            Group group = document.toObject(Group.class);
            if (group.getName() != null) {
                groupsNames.add(group.getName());
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

        ArrayList<String> participantsIds = new ArrayList<String>(studentIDs);
        ArrayList<GroupParticipant> participants = new ArrayList<GroupParticipant>();


        fStore.collection("Students").whereIn(FieldPath.documentId(), participantsIds).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                participants.add(new GroupParticipant((String) document.get("FullName"), document.getId()));
            }

            fStore.collection("Teachers").document(fAuth.getUid()).get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    participants.add(new GroupParticipant((String) documentSnapshot.get("FullName"), documentSnapshot.getId()));
                    participantsIds.add(fAuth.getUid());

                    Group group = new Group(
                            "Grupo " + identifier,
                            fAuth.getUid(),
                            (String) documentSnapshot.get("FullName"),
                            selectedCourse,
                            selectedSubject,
                            participantsIds,
                            participants,
                            groupsCollRef.getId());

                    groupsCollRef.add(group);

                }
            });

        });
    }
}
