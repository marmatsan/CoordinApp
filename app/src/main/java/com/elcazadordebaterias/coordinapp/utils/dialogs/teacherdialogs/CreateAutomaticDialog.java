package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.firebase.firestore.QuerySnapshot;

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
    TextView textview4;

    // Checkbox
    CheckBox checkBox;

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

        textview4 = view.findViewById(R.id.textview4);

        checkBox = view.findViewById(R.id.checkBox);

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

        // Populate subjectNames, and selection behaviour
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

        // Modes selection behaviour
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                checkBox.setVisibility(View.GONE);

                if(selectedOption.equals("Crear por número de alumnos por grupo")){
                    textview4.setText(R.string._4_introduce_el_n_mero_de_alumnos_por_grupo);
                } else if (selectedOption.equals("Crear por número de grupos")) {
                    textview4.setText(R.string._4_introduce_el_n_mero_de_grupos);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(view)
                .setTitle("Menú de creación de grupos")
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    // Just closes the dialog
                }).setPositiveButton("Solicitar", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {

            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener(view1 -> {
                String selectedCourse = courseSpinner.getSelectedItem().toString();
                String selectedSubject = subjectSpinner.getSelectedItem().toString();
                String selectedMode = modeSpinner.getSelectedItem().toString();

                int selectedNumber = Integer.parseInt(numberInput.getText().toString());

                fStore.collection("CoursesOrganization").document(selectedCourse)
                        .collection("Subjects").document(selectedSubject)
                        .get().addOnSuccessListener(documentSnapshot -> {
                            Subject subject = documentSnapshot.toObject(Subject.class);
                            String teacherID = subject.getTeacherID();

                            ArrayList<String> studentIDs = subject.getStudentIDs();
                            Collections.shuffle(studentIDs);

                            if(selectedMode.equals("Introducir el número de alumnos por grupo")){

                                if(selectedNumber >= studentIDs.size()){
                                    Toast.makeText(context, "El tamaño del grupo deseado es más grande que el número total de alumnos. Seleccione un número más pequeño", Toast.LENGTH_LONG).show();
                                } else if (selectedNumber == 0){
                                    Toast.makeText(context, "No puedes crear un grupo de 0 alumnos. Seleccione un número más grande", Toast.LENGTH_SHORT).show();
                                } else if (selectedNumber == 1){
                                    Toast.makeText(context, "No puedes crear un grupo con solo 1 alumno. Seleccione un número más grande", Toast.LENGTH_SHORT).show();
                                } else {
                                    int reminder = studentIDs.size() % selectedNumber;
                                    int numGroups = studentIDs.size() / selectedNumber;

                                    if (numGroups <= 1) {
                                        Toast.makeText(context, "Con este número de alumnos se crearía únicamente un grupo. Seleccione un número de almunos por grupo más pequeño", Toast.LENGTH_LONG).show();
                                    } else {
                                        if (reminder != 0 && !checkBox.isChecked()){
                                            checkBox.setVisibility(View.VISIBLE);
                                            Toast.makeText(context, "No se pueden crear grupos de exactamente " + selectedNumber + " alumnos. Un alumno se quedará sin grupo . Si desea incluirlo en un grupo de un tamaño más grande a " + selectedNumber + " alumnos, marque la casilla ", Toast.LENGTH_LONG).show();
                                        } else if (reminder == 0 || checkBox.isChecked()) {
                                            createGroupsBatch(selectedCourse, selectedSubject, teacherID, numGroups, reminder, studentIDs);
                                            dialog.dismiss();
                                        }
                                    }
                                }
                            } else if (selectedMode.equals("Introducir el número de grupos")){
                                if (selectedNumber == 0) {
                                    Toast.makeText(context, "El número de grupos tiene que ser mayor que 0", Toast.LENGTH_LONG).show();
                                } else if (selectedNumber == 1) {
                                    Toast.makeText(context, "No se puede crear un solo grupo", Toast.LENGTH_LONG).show();
                                } else {
                                    createGroupsBatch(selectedCourse, selectedSubject, teacherID, selectedNumber, 0, studentIDs);
                                }
                            }

                });
            });
        });

        return dialog;
    }

    private void createGroupsBatch(String selectedCourse, String selectedSubject, String teacherID,  int numGroups, int reminder, ArrayList<String> studentIDs){
        ArrayList<List<String>> subLists = new ArrayList<List<String>>();

        for (int i = 0; i < numGroups; i++) {
            List<String> subList = studentIDs.subList(i * numGroups, i * numGroups + numGroups);
            subLists.add(subList);
        }

        if (reminder != 0) {
            List<String> lastList = subLists.get(subLists.size() - 1);
            lastList.add(studentIDs.get(studentIDs.size() - 1));
        }

        for (int i = 0; i < subLists.size(); i++) {
            createGroup(selectedCourse, selectedSubject, subLists.get(i), teacherID);
        }

        Toast.makeText(context, "Grupos creados correctamente", Toast.LENGTH_LONG).show();
    }

    private void createGroup(String course, String subject, List<String> studentIDs, String teacherID) {

        ArrayList<String> participantsIds = new ArrayList<String>(studentIDs);

        ArrayList<GroupParticipant> participants = new ArrayList<GroupParticipant>();

        fStore.collection("Students").whereIn(FieldPath.documentId(), participantsIds).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
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

        });
    }

}
