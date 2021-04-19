package com.elcazadordebaterias.coordinapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;

import com.elcazadordebaterias.coordinapp.adapters.CreateGroupDialogParticipantsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to create the pop-up dialog to create a new chat group. The requester selects a course, a
 * subject, and then selects the participants to make the group.
 *
 * @author Martín Mateos Sánchez
 * @see CreateGroupDialogSpinnerItem
 * @see CreateGroupDialogParticipantsAdapter
 */

public class CreateGroupDialog extends DialogFragment {
    private Spinner courseListSpinner, subjectListSpinner, participantsListSpinner;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) { // TODO: Totally improve this class for errors
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.utils_creategroupdialog, null);


        // All the spinners
        courseListSpinner = view.findViewById(R.id.courseNameSpinner);
        subjectListSpinner = view.findViewById(R.id.subjectNameSpinner);
        participantsListSpinner = view.findViewById(R.id.participantsSpinner);


        /* All the arraylists that we are going to need for populating the spinners.
         * 1. ArrayList<String> coursesNames: The name of the courses
         * 2. ArrayList<String> subjectNames: The name of the subjects of the selected course
         * 3. ArrayList<CreateGroupDialogSpinnerItem> participantsList: The participants of the selected subject in the selected course
         */

        ArrayList<String> coursesNames = new ArrayList<String>();
        ArrayList<String> subjectNames = new ArrayList<String>();
        ArrayList<CreateGroupDialogSpinnerItem> participantsList = new ArrayList<>();

        /* All the adapters that we are going to need for the spinners.
         * 1. ArrayAdapter<String> courseListAdapter: The adapter of courseListSpinner
         * 2. ArrayAdapter<String> subjectListAdapter: The adapter of subjectListSpinner
         * 3. CreateGroupDialogParticipantsAdapter participantsListAdapter: The adapter of participantsListSpinner
         */

        // Group adapter
        ArrayAdapter<String> courseListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, coursesNames);

        courseListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseListSpinner.setAdapter(courseListAdapter);
        courseListSpinner.setSelection(0);

        // Group list spinner

        fStore.collection("CoursesOrganization").get().addOnCompleteListener(task -> { // Get group names
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    coursesNames.add(document.getId());
                }
            }
            courseListAdapter.notifyDataSetChanged();
        });

        // Subjects adapter
        ArrayAdapter<String> subjectListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subjectNames);

        subjectListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectListSpinner.setAdapter(subjectListAdapter);
        subjectListSpinner.setSelection(0);

        // Participants adapter
        CreateGroupDialogParticipantsAdapter participantsListAdapter = new CreateGroupDialogParticipantsAdapter(getContext(), participantsList);
        participantsListSpinner.setAdapter(participantsListAdapter);

        // Listeners when we select the items

        // Course spinner listener
        courseListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectNames.clear();

                String selectedCourseName = parent.getItemAtPosition(position).toString();

                fStore.collection("CoursesOrganization").document(selectedCourseName).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) document.get("Subjects"); // Array of the subjects. Contains all the subjects from the current course

                            for (int i = 0; i < data.size(); i++) { // Iterate over all the subjects in the current course
                                Map<String, Object> subjectInfo = data.get(i); // Current subject information (the list with the students, the name of the subject and the teacher id)
                                subjectNames.add(subjectInfo.get("SubjectName").toString());
                            }

                            subjectListAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Subject spinner listener
        subjectListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                participantsList.clear();
                String selectedSubjectName = parent.getItemAtPosition(position).toString();

                fStore.collection("CoursesOrganization").document(courseListSpinner.getSelectedItem().toString()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) document.get("Subjects"); // Array of the subjects. Contains all the subjects from the current course.

                            for (int i = 0; i < data.size(); i++) { // Iterate over all the subjects in the current course.
                                Map<String, Object> subjectInfo = data.get(i); // Current subject information (the list with the students, the name of the subject and the teacher id).

                                if (subjectInfo.get("SubjectName").toString().equals(selectedSubjectName)) {

                                    ArrayList<String> studentsIds = (ArrayList<String>) subjectInfo.get("Students");

                                    fStore.collection("Students").get().addOnCompleteListener(task1 -> { // Search for student info to build the student list.
                                        if (task1.isSuccessful()) {
                                            for (QueryDocumentSnapshot document1 : task1.getResult()) { // Create the list of the students. The requester name will be displayed first.
                                                if (studentsIds.contains(document1.getId())) {
                                                    if(document1.getId().equals(fAuth.getUid()))
                                                        participantsList.add(new CreateGroupDialogSpinnerItem(document1.getData().get("FullName").toString(), document1.getId(), true, false));
                                                    else
                                                        participantsList.add(new CreateGroupDialogSpinnerItem(document1.getData().get("FullName").toString(), document1.getId(), false, false));
                                                }
                                            }
                                            participantsListAdapter.notifyDataSetChanged();
                                        }
                                    }); 
                                    break;
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        builder.setView(view).setTitle("Solicitud para crear un grupo")
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    // Just closes the dialog
                })
                .setPositiveButton("Solicitar", (dialogInterface, i) -> {

                    String course = courseListSpinner.getSelectedItem().toString();
                    String subject = subjectListSpinner.getSelectedItem().toString();
                    String requesterId = fAuth.getUid();

                    ArrayList<PetitionUser> petitionUsersList = new ArrayList<PetitionUser>();
                    ArrayList<String> petitionUsersIds = new ArrayList<String>();

                    // Add all the students to the petition.
                    for (CreateGroupDialogSpinnerItem item : participantsList) {
                        if (item.isSelected()) {
                            String participantId = item.getParticipantId();
                            petitionUsersList.add(new PetitionUser(participantId, item.getParticipantName(), item.isTeacher(), 0));
                            petitionUsersIds.add(participantId);
                        }
                    }

                    //Add the teacher
                    fStore.collection("CoursesOrganization").document(course).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) document.get("Subjects"); // Array of the subjects. Contains all the subjects from the current course.
                                String teacherId = null;

                                for(Map<String, Object> currentSubject : data){
                                    if(currentSubject.get("SubjectName").equals(subject)){
                                        teacherId = (String) currentSubject.get("TeacherId");
                                        break;
                                    }
                                }

                                String finalTeacherId = teacherId;
                                fStore.collection("Teachers").document(teacherId).get().addOnSuccessListener(documentSnapshot -> {
                                    petitionUsersList.add(new PetitionUser(finalTeacherId, (String) documentSnapshot.getData().get("FullName"), true, 0));
                                    petitionUsersIds.add(finalTeacherId);

                                    fStore.collection("Students").document(requesterId).get().addOnSuccessListener(documentSnapshot1 -> {
                                        PetitionRequest currentPetition = new PetitionRequest(course, subject, requesterId, (String) documentSnapshot1.getData().get("FullName"), petitionUsersIds, petitionUsersList);
                                        fStore.collection("Petitions").add(currentPetition);
                                    });
                                });
                            }
                        }
                    });
                });

        return builder.create();
    }

}
