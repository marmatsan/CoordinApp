package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dialog accessible only by the teacher to let the app create a group based on some rules.
 *
 * @author Martín Mateos Sánchez
 */
public class CreateAutomaticDialog extends DialogFragment {

    // Spinners
    private Spinner courseSpinner, subjectSpinner, modeSpinner;

    // EditText
    private EditText numberInput;

    // Textview if we choose the second option
    TextView textview5;

    // Firestore
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.utils_createautomaticdialog, null);

        // Views
        courseSpinner =  view.findViewById(R.id.courseSpinner);
        subjectSpinner = view.findViewById(R.id.subjectSpinner);
        modeSpinner = view.findViewById(R.id.modeSpinner);
        numberInput = view.findViewById(R.id.numberInput);

        textview5 = view.findViewById(R.id.textview5);

        // Course adapter
        ArrayList<String> coursesNames = new ArrayList<String>();
        ArrayAdapter<String> courseListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, coursesNames);

        courseListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseListAdapter);
        courseSpinner.setSelection(0);

        // Subjects adapter
        ArrayList<String> subjectNames = new ArrayList<String>();
        ArrayAdapter<String> subjectListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subjectNames);

        subjectListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectListAdapter);
        subjectSpinner.setSelection(0);

        // Mode adapter
        ArrayList<String> modes = new ArrayList<String>();
        modes.add("Introducir el número de alumnos por grupo");
        modes.add("Introducir el número de grupos");

        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, modes);

        subjectListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);
        modeSpinner.setSelection(0);

        // Populate coursesNames
        fStore.collection("CoursesOrganization").whereArrayContains("allParticipantsIDs", fAuth.getUid()).get().addOnCompleteListener(task -> { // Get group names
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    coursesNames.add(document.getId());
                }
            }
            courseListAdapter.notifyDataSetChanged();
        });

        // Populate subjectNames
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
                    String selectedNumer = numberInput.getText().toString();

                    if (selectedMode.equals("Introducir el número de alumnos por grupo")) {
                        fStore.collection("CoursesOrganization").document(selectedCourse)
                                .collection("Subjects").document(selectedSubject)
                                .get().addOnSuccessListener(documentSnapshot -> {
                                    Subject subject = documentSnapshot.toObject(Subject.class);

                                    ArrayList<String> studentIDs = subject.getStudentIDs();
                                    Collections.shuffle(studentIDs);

                                    String teacherID = subject.getTeacherID();
                                    int splitNumber = Integer.parseInt(selectedNumer);

                                    int totalGroups = (int) Math.floor(studentIDs.size()/splitNumber);
                                    int remainderStudents = studentIDs.size() % splitNumber;

                                    if(remainderStudents == 1) {
                                        Toast.makeText(context, "Un estudiante se quedará sin grupo. Introduce otro número", Toast.LENGTH_SHORT).show();
                                    }else{
                                        for (int j = 0; j < totalGroups; j++) {
                                            createGroup(selectedCourse, selectedSubject, studentIDs.subList(j * splitNumber, j * splitNumber + splitNumber), teacherID);
                                        }

                                        if (remainderStudents != 0) {
                                            createGroup(selectedCourse, selectedSubject, studentIDs.subList(studentIDs.size() - remainderStudents, studentIDs.size()), teacherID);
                                        }
                                    }
                                });
                    } else {

                    }
                });

        return builder.create();
    }

    private void createGroup(String course, String subject, List<String> studentIDs, String teacherID) {

        ArrayList<String> participantsIds = new ArrayList<String>(studentIDs);

        ArrayList<GroupParticipant> participants = new ArrayList<GroupParticipant>();

        fStore.collection("Students").whereIn(FieldPath.documentId(), participantsIds).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                   participants.add(new GroupParticipant((String) document.get("FullName"), document.getId()));
                }

                fStore.collection("Teachers").document(teacherID).get().addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
                        participants.add(new GroupParticipant((String) documentSnapshot.get("FullName"), documentSnapshot.getId()));
                        participantsIds.add(teacherID);

                        Group group = new Group(
                                fAuth.getUid(),
                                (String) documentSnapshot.get("FullName"),
                                course,
                                subject,
                                participantsIds,
                                participants);

                        group.commit(fStore);

                    }
                });
            }
        });
    }

}
