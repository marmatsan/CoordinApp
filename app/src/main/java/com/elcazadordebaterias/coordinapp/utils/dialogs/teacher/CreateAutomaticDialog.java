package com.elcazadordebaterias.coordinapp.utils.dialogs.teacher;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.CreateGroupDialogParticipantsAdapter;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CreateAutomaticDialog extends DialogFragment {

    private Spinner courseSpinner, subjectSpinner, modeSpinner;
    private EditText numberInput;

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
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.utils_createautomaticdialog, null);

        courseSpinner =  view.findViewById(R.id.courseSpinner);
        subjectSpinner = view.findViewById(R.id.subjectSpinner);
        modeSpinner = view.findViewById(R.id.modeSpinner);
        numberInput = view.findViewById(R.id.numberInput);

        ArrayList<String> coursesNames = new ArrayList<String>();
        ArrayList<String> subjectNames = new ArrayList<String>();

        // Course adapter
        ArrayAdapter<String> courseListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, coursesNames);

        courseListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseListAdapter);
        courseSpinner.setSelection(0);

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
        subjectSpinner.setAdapter(subjectListAdapter);
        subjectSpinner.setSelection(0);

        // Mode adapter
        String[] modes = new String[]{"1"};
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, modes);

        subjectListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);
        modeSpinner.setSelection(0);

        // Listeners when we select the items
        // Course spinner listener
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectNames.clear();
                String selectedCourseName = parent.getItemAtPosition(position).toString();

                fStore.collection("CoursesOrganization").document(selectedCourseName).collection("Subjects").whereEqualTo("teacherID", fAuth.getUid()).get().addOnCompleteListener(task -> { // Get group names
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


        builder.setView(view).setTitle("Menú de creación de grupos")
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    // Just closes the dialog
                })
                .setPositiveButton("Solicitar", (dialogInterface, i) -> {

                    String selectedCourse = courseSpinner.getSelectedItem().toString();
                    String selectedSubject = subjectSpinner.getSelectedItem().toString();
                    String selectedMode = modeSpinner.getSelectedItem().toString();
                    String selectedNmuber = numberInput.getText().toString();

                    String requesterId = fAuth.getUid();

                    fStore.collection("CoursesOrganization").document(selectedCourse).collection("Subjects").document(selectedSubject).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                if(document.exists()){
                                    Subject subject = document.toObject(Subject.class);

                                    ArrayList<String> studentIDs = subject.getStudentIDs();

                                }
                            }
                        }
                    });

                });

        return builder.create();
    }
}
