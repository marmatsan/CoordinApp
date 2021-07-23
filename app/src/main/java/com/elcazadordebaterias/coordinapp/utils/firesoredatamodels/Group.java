package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

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

    public Group() {

    }


    public Group(String coordinatorId, String coordinatorName, String courseName, String subjectName, ArrayList<String> participantsIds, ArrayList<GroupParticipant> participants) {
        this.coordinatorId = coordinatorId;
        this.coordinatorName = coordinatorName;
        this.courseName = courseName;
        this.subjectName = subjectName;
        this.participantsIds = participantsIds;
        this.participants = participants;
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


    // Set the name of the group by searching the max identifier from an array of group Names
    public void setGroupName(ArrayList<String> groupsNames) {
        if (groupsNames.isEmpty()) {
            this.name = "Grupo 1";
        } else {
            ArrayList<Integer> numbers = new ArrayList<Integer>();

            for (String identifier : groupsNames) {
                Log.d("DEBUGGING", "" + groupsNames);
                String numberOnly = identifier.replaceAll("[^0-9]", "");
                numbers.add(Integer.parseInt(numberOnly));
            }

            int maxNumber = Collections.max(numbers);
            int newGroupNumber = maxNumber + 1;

            this.name = "Grupo " + newGroupNumber;
        }
    }

    public void createAndCommit(FirebaseFirestore fStore, Context context) {

        CollectionReference groupsCollRef = fStore.collection("CoursesOrganization")
                .document(getCourseName())
                .collection("Subjects")
                .document(getSubjectName())
                .collection("Groups");

        groupsCollRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<String> groupsNames = new ArrayList<String>();

            for (DocumentSnapshot document : queryDocumentSnapshots) {
                Group group = document.toObject(Group.class);
                if (group.getName() != null) {
                    groupsNames.add(group.getName());
                }
            }

            setGroupName(groupsNames);

            groupsCollRef.add(this).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Grupo creado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Error al crear el grupo", Toast.LENGTH_SHORT).show();
                }
            });

        });

    }
}
