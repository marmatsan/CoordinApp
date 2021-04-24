package com.elcazadordebaterias.coordinapp.utils.dialogs.student;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;

import com.elcazadordebaterias.coordinapp.adapters.CreateGroupDialogParticipantsAdapter;
import com.elcazadordebaterias.coordinapp.utils.PetitionRequest;
import com.elcazadordebaterias.coordinapp.utils.PetitionUser;
import com.elcazadordebaterias.coordinapp.utils.dialogs.CreateGroupDialogSpinnerItem;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

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
        View view = inflater.inflate(R.layout.utils_creategroupdialog_student, null);


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

        // Course adapter
        ArrayAdapter<String> courseListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, coursesNames);

        courseListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseListSpinner.setAdapter(courseListAdapter);
        courseListSpinner.setSelection(0);

        // Course list spinner
        fStore.collection("CoursesOrganization").whereArrayContains("allParticipantsIDs", fAuth.getUid()).get().addOnCompleteListener(task -> { // Get group names
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

                fStore.collection("CoursesOrganization").document(selectedCourseName).collection("Subjects").whereArrayContains("studentIDs", fAuth.getUid()).get().addOnCompleteListener(task -> { // Get group names
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            subjectNames.add(document.getId());
                        }
                    }
                    subjectListAdapter.notifyDataSetChanged();
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

                fStore.collection("CoursesOrganization").document(courseListSpinner.getSelectedItem().toString()).collection("Subjects").document(selectedSubjectName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Subject subject = documentSnapshot.toObject(Subject.class);

                        fStore.collection("Students").whereIn(FieldPath.documentId(), subject.getStudentIDs()).get().addOnCompleteListener(getStudentsInfo -> { // Get the information of the students
                            if (getStudentsInfo.isSuccessful()) {

                                for (QueryDocumentSnapshot studentDocument : getStudentsInfo.getResult()) {
                                    if(studentDocument.getId().equals(fAuth.getUid()))
                                        participantsList.add(new CreateGroupDialogSpinnerItem(studentDocument.getData().get("FullName").toString(), studentDocument.getId(), true, false));
                                    else
                                        participantsList.add(new CreateGroupDialogSpinnerItem(studentDocument.getData().get("FullName").toString(), studentDocument.getId(), false, false));
                                }

                                participantsListAdapter.notifyDataSetChanged();
                            }
                        });
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

                    String selectedCourse = courseListSpinner.getSelectedItem().toString();
                    String selectedSubject = subjectListSpinner.getSelectedItem().toString();
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

                    if(petitionUsersList.size() <= 1){
                        Toast.makeText(getContext(), "Debes agregar al menos a un miembro más al grupo", Toast.LENGTH_SHORT).show();
                    } else {
                        fStore.collection("CoursesOrganization").document(selectedCourse).collection("Subjects").document(selectedSubject).get().addOnSuccessListener(subjectDocument -> {
                            Subject subject = subjectDocument.toObject(Subject.class);

                            String teacherID = subject.getTeacherID();

                            fStore.collection("Teachers").document(teacherID).get().addOnSuccessListener(teacherDocument -> {
                                petitionUsersList.add(new PetitionUser(teacherDocument.getId(), (String) teacherDocument.getData().get("FullName"), true, 0));
                                petitionUsersIds.add(teacherID);

                                fStore.collection("Students").document(requesterId).get().addOnSuccessListener(requesterDocument -> {
                                    PetitionRequest currentPetition = new PetitionRequest(selectedCourse, selectedSubject, requesterId, (String) requesterDocument.getData().get("FullName"), petitionUsersIds, petitionUsersList);
                                    fStore.collection("Petitions").add(currentPetition);
                                });
                            });
                        });
                    }

                });

        return builder.create();
    }

}
