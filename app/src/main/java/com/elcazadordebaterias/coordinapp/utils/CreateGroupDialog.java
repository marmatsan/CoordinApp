package com.elcazadordebaterias.coordinapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;

import com.elcazadordebaterias.coordinapp.adapters.CreateGroupDialogParticipantsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to create the pop-up dialog to create a new chat group
 *
 * @author Martín Mateos Sánchez
 */

public class CreateGroupDialog extends DialogFragment {
    private Spinner courseListSpinner, subjectListSpinner, participantsListSpinner;

    private CreateGroupDialogListener listener;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        try {
            listener = (CreateGroupDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CreateGroupDialogListener");
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
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
        courseListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectNames.clear();

                String selectedCourseName = parent.getItemAtPosition(position).toString();

                fStore.collection("CoursesOrganization").document(selectedCourseName).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) document.get("Subjects"); // Array of the subjects. Contains all the subjects from the current course
                            List<CourseSubject> courseSubjectList = new ArrayList<>();  // List with the information of the subjects

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
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Subject spinner listener
        subjectListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                participantsList.clear();
                String selectedSubjectName = parent.getItemAtPosition(position).toString();

                fStore.collection("CoursesOrganization").document(courseListSpinner.getSelectedItem().toString()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) document.get("Subjects"); // Array of the subjects. Contains all the subjects from the current course

                            for (int i = 0; i < data.size(); i++) { // Iterate over all the subjects in the current course
                                Map<String, Object> subjectInfo = data.get(i); // Current subject information (the list with the students, the name of the subject and the teacher id)

                                if(subjectInfo.get("SubjectName").toString().equals(selectedSubjectName)){

                                    ArrayList<String> studentsIds = (ArrayList<String>) subjectInfo.get("Students");

                                    fStore.collection("Students").get().addOnCompleteListener(task1 -> { // Search for student info to build the student list
                                        if (task1.isSuccessful()) {
                                            // Add the teacher to be displayed
                                            fStore.collection("Teachers").document((String) subjectInfo.get("TeacherId")).get().addOnCompleteListener(task2 -> {
                                                if (task2.isSuccessful()) {

                                                    DocumentSnapshot document2 = task2.getResult();

                                                    participantsList.add(new CreateGroupDialogSpinnerItem("Profesor: " + document2.getData().get("FullName").toString(),true , true));

                                                    for (QueryDocumentSnapshot document1 : task1.getResult()) { // Create the list of the students
                                                        if (studentsIds.contains(document1.getId()) && !document1.getId().equals(fAuth.getCurrentUser().getUid())) { // The current user is not shown in the list
                                                            participantsList.add(new CreateGroupDialogSpinnerItem(document1.getData().get("FullName").toString(), false, false));
                                                        }
                                                    }
                                                    participantsListAdapter.notifyDataSetChanged();
                                                }
                                            });
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
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        builder.setView(view).setTitle("Solicitud para crear un grupo")
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    // Just closes the dialog
        })
                .setPositiveButton("Solicitar", (dialogInterface, i) -> {

                    String course = courseListSpinner.getSelectedItem().toString();
                    String subject = subjectListSpinner.getSelectedItem().toString();
                    ArrayList<CreateGroupDialogSpinnerItem> selectedParticipants = new ArrayList<CreateGroupDialogSpinnerItem>();

                    for(CreateGroupDialogSpinnerItem item : participantsList){
                        if(item.isSelected()){
                            selectedParticipants.add(item);
                        }
                    }

                    listener.submitRequest(course, subject, selectedParticipants);

        });

        return builder.create();
    }

    public interface CreateGroupDialogListener {
        void submitRequest(String course, String subject, ArrayList<CreateGroupDialogSpinnerItem> participants);
    }

}
