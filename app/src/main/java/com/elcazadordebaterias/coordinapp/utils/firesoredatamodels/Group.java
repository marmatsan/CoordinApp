package com.elcazadordebaterias.coordinapp.utils.firesoredatamodels;

import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupParticipant;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class that represents the data of the group an user is in FireStore. Used when creating a new group,
 * accepted by the teacher.
 *
 * @author Martín Mateos Sánchez
 */
public class Group {

    private String groupName; // The name of the group
    private String coordinatorId; // The id of the teacher that created the group
    private String coordinatorName; // The name of the teacher that created the group
    private String courseName;     // The name of this group
    private String subjectName;

    private ArrayList<String> participantsIds;
    private ArrayList<GroupParticipant> participants;

    public Group(){

    }

    public Group(String coordinatorId, String coordinatorName, String courseName, String subjectName, ArrayList<String> participantsIds, ArrayList<GroupParticipant> participants){
        this.coordinatorId = coordinatorId;
        this.coordinatorName = coordinatorName;
        this.courseName = courseName;
        this.subjectName = subjectName;
        this.participantsIds = participantsIds;
        this.participants = participants;
    }

    public String getGroupName() {
        return groupName;
    }

    // Set the name of the group by searching the max identifier from an array of String identifiers
    public void setGroupName(ArrayList<String> groupsIdentifiers) {
        if (groupsIdentifiers.isEmpty()) {
            this.groupName = "Grupo 1";
        } else {
            ArrayList<Integer> numbers = new ArrayList<Integer>();

            for (String identifier : groupsIdentifiers){
                String numberOnly = identifier.replaceAll("[^0-9]", "");
                numbers.add(Integer.parseInt(numberOnly));
            }

            int maxNumber = Collections.max(numbers);
            int newGroupNumber = maxNumber + 1;

            this.groupName = "Grupo " + newGroupNumber;
        }
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

    public void commit(FirebaseFirestore fStore){
        fStore.collection("CoursesOrganization")
                .document(this.getCourseName())
                .collection("Subjects")
                .document(this.getSubjectName())
                .collection("Groups").add(this);
    }

}
