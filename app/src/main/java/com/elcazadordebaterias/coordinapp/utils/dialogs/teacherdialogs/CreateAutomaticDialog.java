package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateAutomaticDialog extends DialogFragment {

    // Selected course and subject
    private String selectedCourse;
    private String selectedSubject;

    // Radiogroup
    private RadioGroup radioGroup;
    private int checkedRadioButtonId;

    private int inputNumber;

    // Views to be shown
    private TextView modeTitle;
    private EditText numberInput;
    private CheckBox checkBox;
    private View separator;
    private TextView errorMessageView;

    // Firestore
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    // Context
    Context context;

    // Custom error message
    String customErrorMessage;

    // Reference to the subject of the selected course
    DocumentReference subjectRef;

    public CreateAutomaticDialog(String selectedCourse, String selectedSubject) {
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        subjectRef = fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.utils_dialogs_createautomaticdialog, null);

        // Views
        radioGroup = view.findViewById(R.id.radioGroup);

        modeTitle = view.findViewById(R.id.modeTitle);
        modeTitle.setVisibility(View.GONE);

        numberInput = view.findViewById(R.id.numberInput);
        numberInput.setVisibility(View.GONE);

        checkBox = view.findViewById(R.id.checkBox);
        checkBox.setVisibility(View.GONE);

        separator = view.findViewById(R.id.separator);
        separator.setVisibility(View.GONE);

        errorMessageView = view.findViewById(R.id.errorMessage);
        errorMessageView.setVisibility(View.GONE);

        // RadioGroup configuration
        checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            checkedRadioButtonId = checkedId;
            if (checkedId == R.id.radio_button_1) {
                modeTitle.setText(R.string._2_introduce_el_n_mero_de_alumnos_por_grupo);
                separator.setVisibility(View.GONE);
                errorMessageView.setVisibility(View.GONE);
                checkBox.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.radio_button_2) {
                modeTitle.setText(R.string._2_introduce_el_n_mero_de_grupos);
                separator.setVisibility(View.GONE);
                errorMessageView.setVisibility(View.GONE);
                checkBox.setVisibility(View.GONE);
            }
            modeTitle.setVisibility(View.VISIBLE);
            numberInput.setVisibility(View.VISIBLE);
        });

        builder.setView(view)
                .setTitle("Menú de creación de grupos")
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    // Just closes the dialog
                }).setPositiveButton("Crear grupos", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {

            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener(view1 -> {

                COMPLETE_OP_CODE completeExternalCode = COMPLETE_OP_CODE.NO_ERROR;
                boolean displayExternalError = true;

                if (checkedRadioButtonId == View.NO_ID) {
                    completeExternalCode = COMPLETE_OP_CODE.NO_OPTION_SELECTED;
                } else if (numberInput.getText().toString().isEmpty()) {
                    completeExternalCode = COMPLETE_OP_CODE.NO_INPUT_NUMBER;
                } else {

                    inputNumber = Integer.parseInt(numberInput.getText().toString());

                    if (inputNumber < 0) {
                        completeExternalCode = COMPLETE_OP_CODE.INPUT_NUMBER_NEGATIVE;
                    } else if (inputNumber == 0) {
                        completeExternalCode = COMPLETE_OP_CODE.INPUT_NUMBER_ZERO;
                    } else {
                        displayExternalError = false;
                        subjectRef
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    COMPLETE_OP_CODE completeInternalCode = COMPLETE_OP_CODE.NO_ERROR;

                                    Subject subject = documentSnapshot.toObject(Subject.class);
                                    ArrayList<String> studentIDs = subject.getStudentIDs();

                                    Collections.shuffle(studentIDs);

                                    SELECTED_MODE mode = getSelectedMode();

                                    int studentsPerGroup = 0;
                                    int numGroups = 0;
                                    int remainder = 0;

                                    if (mode == SELECTED_MODE.STUDENTS_PER_GROUP) {

                                        studentsPerGroup = inputNumber;
                                        numGroups = studentIDs.size() / studentsPerGroup;
                                        remainder = studentIDs.size() % studentsPerGroup;

                                    } else if (mode == SELECTED_MODE.NUMBER_OF_GROUPS) {

                                        numGroups = inputNumber;
                                        studentsPerGroup = studentIDs.size() / numGroups;
                                        remainder = studentIDs.size() % numGroups;

                                    }

                                    if (numGroups < 1) {
                                        completeInternalCode = COMPLETE_OP_CODE.ZERO_GROUPS;
                                    } else if (numGroups > studentIDs.size()) {
                                        completeInternalCode = COMPLETE_OP_CODE.MORE_GROUPS_THAN_STUDENTS;
                                    }

                                    if (completeInternalCode == COMPLETE_OP_CODE.NO_ERROR) {
                                        createGroupsBatch(studentsPerGroup, numGroups, remainder, studentIDs);
                                        dialog.dismiss();
                                    } else {
                                        displayError(completeInternalCode);
                                    }

                                });
                    }
                }
                if (displayExternalError) {
                    displayError(completeExternalCode);
                }
            });
        });

        return dialog;
    }

    private void createGroupsBatch(int studentsPerGroup, int numGroups, int remainder, ArrayList<String> studentIDs) {
        CollectionReference groupsCollRef;
        ArrayList<List<String>> subLists = new ArrayList<List<String>>();

        if (numGroups == studentIDs.size()) { // We want to create individual chats of all the students
            groupsCollRef = subjectRef.collection("IndividualGroups");
        } else {
            groupsCollRef = subjectRef.collection("CollectiveGroups");
        }

        for (int i = 0; i < numGroups; i++) {
            List<String> subList = studentIDs.subList(i * studentsPerGroup, i * studentsPerGroup + studentsPerGroup);
            subLists.add(subList);
        }

        if (remainder != 0) {

            boolean isChecked;
            SELECTED_MODE mode = getSelectedMode();

            if (mode == SELECTED_MODE.NUMBER_OF_GROUPS || (mode == SELECTED_MODE.STUDENTS_PER_GROUP && remainder == 1)) {
                isChecked = true;
            } else {
                isChecked = checkBox.isChecked();
            }

            List<String> lastList;
            if (isChecked) { // Add the remainder students to a group greater than the specified group
                lastList = new ArrayList<String>(subLists.get(subLists.size() - 1));
                subLists.remove(subLists.size() - 1);

                for (int i = 0; i < remainder; i++) {
                    lastList.add(studentIDs.get(studentIDs.size() - i - 1));
                }

            } else { // Add the remainder students to a separate group
                lastList = new ArrayList<String>();

                for (int i = 0; i < remainder; i++) {
                    lastList.add(studentIDs.get(studentIDs.size() - i - 1));
                }

            }
            subLists.add(lastList);

        }

        groupsCollRef
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    int maxIdentifier = Group.getMaxGroupIdentifier(queryDocumentSnapshots);

                    // Create the groups
                    for (int i = 0; i < subLists.size(); i++) {
                        Group.createGroup(groupsCollRef, selectedCourse, selectedSubject, subLists.get(i), maxIdentifier + 1 + i, context, null, null);
                    }

                });
    }

    private enum COMPLETE_OP_CODE {
        NO_ERROR,
        NO_OPTION_SELECTED,
        NO_INPUT_NUMBER,
        INPUT_NUMBER_NEGATIVE,
        INPUT_NUMBER_ZERO,
        ZERO_GROUPS,
        MORE_GROUPS_THAN_STUDENTS
    }

    private enum SELECTED_MODE {
        STUDENTS_PER_GROUP, NUMBER_OF_GROUPS, NO_MODE_SELECTED
    }

    private String getErrorMessage(COMPLETE_OP_CODE completeCode) {
        String errorMessage = null;
        SELECTED_MODE mode = getSelectedMode();

        if (completeCode == COMPLETE_OP_CODE.NO_ERROR) {
            errorMessage = "Todo correcto";
        } else if (completeCode == COMPLETE_OP_CODE.NO_OPTION_SELECTED) {
            errorMessage = "No se ha seleccionado ninún modo de creación de grupos";
        } else if (completeCode == COMPLETE_OP_CODE.NO_INPUT_NUMBER) {
            errorMessage = "No se ha introducido ningún número. Por favor, introduzca un número válido";
        } else if (completeCode == COMPLETE_OP_CODE.INPUT_NUMBER_NEGATIVE) {
            errorMessage = "No puedes introducir un número negativo. Introduce un número mayor que 0";
        } else if (completeCode == COMPLETE_OP_CODE.INPUT_NUMBER_ZERO) {
            if (mode == SELECTED_MODE.STUDENTS_PER_GROUP) {
                errorMessage = "No puedes crear grupos de 0 alumnos. Introduce un número mayor que 0";
            } else if (mode == SELECTED_MODE.NUMBER_OF_GROUPS) {
                errorMessage = "Tienes que crear al menos un grupo";
            }
        } else if (completeCode == COMPLETE_OP_CODE.ZERO_GROUPS) {
            errorMessage = "El número de alumnos por grupo deseado excede el número de alumnos en la asignatura. Escoja un número más pequeño";
        } else if (completeCode == COMPLETE_OP_CODE.MORE_GROUPS_THAN_STUDENTS) {
            errorMessage = "No puedes crear más grupos que alumnos hay en la asignatura";
        } else {
            errorMessage = "Error desconocido";
        }

        return errorMessage;
    }

    private void displayError(COMPLETE_OP_CODE completeCode) {
        String errorMessage = getErrorMessage(completeCode);
        if (errorMessage != null) {
            separator.setVisibility(View.VISIBLE);
            errorMessageView.setText(errorMessage);
            errorMessageView.setVisibility(View.VISIBLE);
        }
    }

    private SELECTED_MODE getSelectedMode() {
        SELECTED_MODE mode;
        if (checkedRadioButtonId == R.id.radio_button_1) {
            mode = SELECTED_MODE.STUDENTS_PER_GROUP;
        } else if (checkedRadioButtonId == R.id.radio_button_2) {
            mode = SELECTED_MODE.NUMBER_OF_GROUPS;
        } else {
            mode = SELECTED_MODE.NO_MODE_SELECTED;
        }
        return mode;
    }

}
