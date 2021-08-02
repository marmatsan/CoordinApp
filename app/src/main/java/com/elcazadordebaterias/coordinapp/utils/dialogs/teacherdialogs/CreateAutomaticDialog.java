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

/**
 * Dialog accessible only by the teacher to let the app create a group based on some rules.
 *
 * @author Martín Mateos Sánchez
 */
public class CreateAutomaticDialog extends DialogFragment {

    // Selected course and subject
    private String selectedCourse;
    private String selectedSubject;

    // Radiogroup
    private RadioGroup radioGroup;
    private int checkedRadioButtonId;

    // EditText
    private EditText numberInput;
    private int inputNumber;

    // Textview if we choose the second option
    TextView textview2;

    // Checkbox
    CheckBox checkBox;

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

        textview2 = view.findViewById(R.id.textview2);
        textview2.setVisibility(View.GONE);

        numberInput = view.findViewById(R.id.numberInput);
        numberInput.setVisibility(View.GONE);

        checkBox = view.findViewById(R.id.checkBox);
        checkBox.setVisibility(View.GONE);

        // RadioGroup configuration
        checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            checkedRadioButtonId = checkedId;
            if (checkedId == R.id.radio_button_1) {
                textview2.setText(R.string._2_introduce_el_n_mero_de_alumnos_por_grupo);
            } else if (checkedId == R.id.radio_button_2) {
                textview2.setText(R.string._2_introduce_el_n_mero_de_grupos);
            }
            textview2.setVisibility(View.VISIBLE);
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
                        subjectRef.get().addOnSuccessListener(documentSnapshot -> {
                            COMPLETE_OP_CODE completeInternalCode = COMPLETE_OP_CODE.NO_ERROR;

                            Subject subject = documentSnapshot.toObject(Subject.class);
                            ArrayList<String> studentIDs = subject.getStudentIDs();

                            Collections.shuffle(studentIDs);

                            SELECTED_MODE mode = getSelectedMode();

                            int studentsPerGroup = 0;
                            int numGroups = 0;
                            int reminder = 0;

                            if (mode == SELECTED_MODE.STUDENTS_PER_GROUP) {

                                studentsPerGroup = inputNumber;
                                numGroups = studentIDs.size() / studentsPerGroup;
                                reminder = studentIDs.size() % studentsPerGroup;

                            } else if (mode == SELECTED_MODE.NUMBER_OF_GROUPS) {

                                numGroups = inputNumber;
                                studentsPerGroup = studentIDs.size() / numGroups;
                                reminder = studentIDs.size() % numGroups;

                            }

                            if (numGroups < 1) {
                                completeInternalCode = COMPLETE_OP_CODE.ZERO_GROUPS;
                            } else {
                                if (reminder != 0 && checkBox.getVisibility() == View.GONE) {
                                    completeInternalCode = COMPLETE_OP_CODE.PENDING_STUDENTS;
                                    checkBox.setVisibility(View.VISIBLE);
                                    setCustomErrorMessage(reminder);
                                }
                            }

                            if (completeInternalCode == COMPLETE_OP_CODE.NO_ERROR) {
                                createGroupsBatch(studentsPerGroup, numGroups, reminder, studentIDs);
                                dialog.dismiss();
                            }

                            displayError(completeInternalCode);
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

    private void createGroupsBatch(int studentsPerGroup, int numGroups, int reminder, ArrayList<String> studentIDs) {
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

        if (checkBox.isChecked()) { // Add the reminder students to a group greater than the specified group
            List<String> lastList = new ArrayList<String>(subLists.get(subLists.size() - 1));
            lastList.add(studentIDs.get(studentIDs.size() - 1));
            subLists.remove(subLists.get(subLists.size() - 1));
            subLists.add(lastList);
        } else { // Add the reminder students to a separate group
            ArrayList<String> lastList = new ArrayList<String>();
            lastList.add(studentIDs.get(studentIDs.size() - 1));
            subLists.add(lastList);
        }

        groupsCollRef.get().addOnSuccessListener(queryDocumentSnapshots -> {

            int maxIdentifier = Group.getMaxGroupIdentifier(queryDocumentSnapshots);

            // Create the groups
            for (int i = 0; i < subLists.size(); i++) {
                createGroup(groupsCollRef, subLists.get(i), maxIdentifier + 1 + i);
            }

        });
    }

    private void createGroup(CollectionReference groupsCollRef, List<String> studentIDs, int identifier) {

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

    private enum COMPLETE_OP_CODE {
        NO_ERROR,
        NO_OPTION_SELECTED,
        NO_INPUT_NUMBER,
        INPUT_NUMBER_NEGATIVE,
        INPUT_NUMBER_ZERO,
        ZERO_GROUPS,
        PENDING_STUDENTS
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
            errorMessage = "No puedes introducir un número negativo. Introduce un número mayor que 0.";
        } else if (completeCode == COMPLETE_OP_CODE.INPUT_NUMBER_ZERO) {
            if (mode == SELECTED_MODE.STUDENTS_PER_GROUP) {
                errorMessage = "No puedes crear grupos de 0 alumnos. Introduce un número mayor que 0.";
            } else if (mode == SELECTED_MODE.NUMBER_OF_GROUPS) {
                errorMessage = "Tienes que crear al menos un grupo en este modo de creación de grupos.";
            }
        } else if (completeCode == COMPLETE_OP_CODE.ZERO_GROUPS) {
            errorMessage = "El número de alumnos por grupo deseado excede el número de alumnos en la asignatura. Seleccione un número más pequeño";
        } else if (completeCode == COMPLETE_OP_CODE.PENDING_STUDENTS && customErrorMessage != null) {
            errorMessage = customErrorMessage;
        } else {
            errorMessage = "Error desconocido";
        }

        return errorMessage;
    }

    private void displayError(COMPLETE_OP_CODE completeCode) {
        String errorMessage = getErrorMessage(completeCode);
        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
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

    private void setCustomErrorMessage(int reminder) {
        if (reminder > 1) {
            customErrorMessage = "No se puede crear un grupo de exactamente " + inputNumber + " alumnos. " + reminder + "alumnos " +
                    "se quedarán sin grupo. Si desea incluirlos en un grupo de un tamaño más grande a " + inputNumber + " alumnos, marque " +
                    "la casilla. De lo contrario, se creará un grupo a parte de " + reminder + " alumnos.";
        } else {
            customErrorMessage = "No se puede crear un grupo de exactamente " + inputNumber + " alumnos. Un alumno " +
                    "se quedará sin grupo. Si desea incluirlo en un grupo de un tamaño más grande a " + inputNumber + " alumnos, marque " +
                    "la casilla. De lo contrario, se creará un grupo a parte con el alumno restante.";
        }
    }

}
