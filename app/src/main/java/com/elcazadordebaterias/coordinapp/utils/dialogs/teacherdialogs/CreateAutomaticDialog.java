package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
                }).setPositiveButton("Solicitar", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {

            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener(view1 -> {

                ERROR_CODE externalError = ERROR_CODE.NO_ERROR;

                if (checkedRadioButtonId == View.NO_ID) {
                    externalError = ERROR_CODE.NO_OPTION_SELECTED;
                    displayError(externalError, null);
                } else if (numberInput.getText().toString().isEmpty()) {
                    externalError = ERROR_CODE.NO_INPUT_NUMBER;
                    displayError(externalError, null);
                } else {

                    inputNumber = Integer.parseInt(numberInput.getText().toString());

                    if (inputNumber < 0) {
                        externalError = ERROR_CODE.INPUT_NUMBER_NEGATIVE;
                        displayError(externalError, null);
                    } else if (inputNumber == 0) {
                        externalError = ERROR_CODE.INPUT_NUMBER_ZERO;
                        displayError(externalError, null);
                    } else {
                        fStore.collection("CoursesOrganization").document(selectedCourse)
                                .collection("Subjects").document(selectedSubject)
                                .get().addOnSuccessListener(documentSnapshot -> {
                                    Subject subject = documentSnapshot.toObject(Subject.class);

                                    ArrayList<String> studentIDs = subject.getStudentIDs();
                                    Collections.shuffle(studentIDs);

                                    ERROR_CODE internalError =  ERROR_CODE.NO_ERROR;
                                    String customErrorMessage = null;
                                    SELECTED_MODE mode = getSelectedMode();

                                    if (mode == SELECTED_MODE.STUDENTS_PER_GROUP){
                                        int numGroups = studentIDs.size() / inputNumber;
                                        int reminder = studentIDs.size() % inputNumber;

                                       if (numGroups < 1){
                                           internalError = ERROR_CODE.ZERO_GROUPS;
                                           displayError(internalError, null);
                                        } else {
                                            boolean chechBoxIsChecked = checkBox.isChecked();

                                            if (reminder != 0 && !chechBoxIsChecked){
                                                checkBox.setVisibility(View.VISIBLE);
                                                internalError = ERROR_CODE.PENDING_STUDENTS;

                                                if (reminder > 1){
                                                    customErrorMessage = "No se puede crear un group de exactamente " + inputNumber + " alumnos." + reminder + "alumnos" +
                                                            "se qudarán sin grupo. Si desea incluirlos en un grupo de un tamaño más grande a " + inputNumber + " alumnos, marque " +
                                                            "la casilla";
                                                } else {
                                                    customErrorMessage = "No se puede crear un group de exactamente " + inputNumber + " alumnos." + reminder + "alumno" +
                                                            "se qudará sin grupo. Si desea incluirlo en un grupo de un tamaño más grande a " + inputNumber + " alumnos, marque " +
                                                            "la casilla";
                                                }
                                                displayError(internalError, customErrorMessage);

                                            } else if (reminder == 0 || chechBoxIsChecked) {
                                                createGroupsBatch(internalError, numGroups, reminder, studentIDs);
                                                dialog.dismiss();
                                            }
                                        }


                                    } else if (mode == SELECTED_MODE.NUMBER_OF_GROUPS) {

                                    }

                                });
                    }

                }

            });
        });

        return dialog;
    }

    private void createGroupsBatch(ERROR_CODE error, int numGroups, int reminder, ArrayList<String> studentIDs) {

        if (error == ERROR_CODE.NO_ERROR) {
            ArrayList<List<String>> subLists = new ArrayList<List<String>>();

            for (int i = 0; i < numGroups; i++) {
                List<String> subList = studentIDs.subList(i * numGroups, i * numGroups + numGroups);
                subLists.add(subList);
            }

            if (reminder != 0) {
                List<String> lastList = subLists.get(subLists.size() - 1);
                lastList.add(studentIDs.get(studentIDs.size() - 1));
            }

            // Search for the maximum identifier of the groups and create them
            CollectionReference groupsCollRef = fStore.collection("CoursesOrganization")
                    .document(selectedCourse)
                    .collection("Subjects")
                    .document(selectedSubject)
                    .collection("Groups");

            groupsCollRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
                ArrayList<String> groupsNames = new ArrayList<String>();

                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    Group group = document.toObject(Group.class);
                    if (group.getName() != null) {
                        groupsNames.add(group.getName());
                    }
                }

                int maxIdentifier = getMaxGroupIdentifier(groupsNames);

                // Create the groups
                for (int i = 0; i < subLists.size(); i++) {
                    createGroup(groupsCollRef, subLists.get(i), maxIdentifier + 1 + i);
                }

            });
        }
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

                    String name = "Grupo " + identifier;

                    Group group = new Group(
                            name,
                            fAuth.getUid(),
                            (String) documentSnapshot.get("FullName"),
                            selectedCourse,
                            selectedSubject,
                            participantsIds,
                            participants);

                    groupsCollRef.add(group);

                }
            });

        });
    }

    private enum ERROR_CODE {
        NO_ERROR,
        NO_OPTION_SELECTED,
        NO_INPUT_NUMBER,
        INPUT_NUMBER_NEGATIVE,
        INPUT_NUMBER_ZERO,
        ZERO_GROUPS,
        PENDING_STUDENTS
    }

    private enum SELECTED_MODE {
        STUDENTS_PER_GROUP, NUMBER_OF_GROUPS
    }

    private String getErrorMessage(ERROR_CODE error, String customErrorMessage) {
        String errorMessage = null;
        SELECTED_MODE mode = getSelectedMode();

        if (error == ERROR_CODE.NO_ERROR) {
            errorMessage = "Todo correcto";
        } else if (error == ERROR_CODE.NO_OPTION_SELECTED) {
            errorMessage = "No se ha seleccionado ninún modo de creación de grupos";
        } else if (error == ERROR_CODE.NO_INPUT_NUMBER) {
            errorMessage = "No se ha introducido ningún número. Por favor, introduzca un número válido";
        } else if (error == ERROR_CODE.INPUT_NUMBER_NEGATIVE) {
            errorMessage = "No puedes introducir un número negativo. Introduce un número mayor que 0.";
        } else if (error == ERROR_CODE.INPUT_NUMBER_ZERO) {
            if (mode == SELECTED_MODE.STUDENTS_PER_GROUP) {
                errorMessage = "No puedes crear grupos de 0 alumnos. Introduce un número mayor que 0.";
            } else if (mode == SELECTED_MODE.NUMBER_OF_GROUPS) {
                errorMessage = "Tienes que crear al menos un grupo en este modo de creación de grupos.";
            }
        } else if (error == ERROR_CODE.ZERO_GROUPS) {
            errorMessage = "El tamaño del grupo deseado es más grande que el número total de alumnos. Seleccione un número más pequeño";
        } else if (error == ERROR_CODE.PENDING_STUDENTS && customErrorMessage != null){
            errorMessage = customErrorMessage;
        } else {
            errorMessage = "Error desconocido";
        }

        return errorMessage;
    }

    private void displayError(ERROR_CODE error, String customErrorMessage){
        String errorMessage = getErrorMessage(error, customErrorMessage);
        if (errorMessage != null){
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    private SELECTED_MODE getSelectedMode(){
        SELECTED_MODE mode = null;
        if (checkedRadioButtonId == R.id.radio_button_1){
            mode = SELECTED_MODE.STUDENTS_PER_GROUP;
        } else if (checkedRadioButtonId == R.id.radio_button_2) {
            mode = SELECTED_MODE.NUMBER_OF_GROUPS;
        }
        return mode;
    }

    private int getMaxGroupIdentifier(ArrayList<String> groupsNames){
        int maxGroupIdentifier = 0;

        if(!groupsNames.isEmpty()) {
            ArrayList<Integer> numbers = new ArrayList<Integer>();

            for (String identifier : groupsNames) {
                String numberOnly = identifier.replaceAll("[^0-9]", "");
                numbers.add(Integer.parseInt(numberOnly));
            }

            maxGroupIdentifier = Collections.max(numbers);
        }

        return maxGroupIdentifier;
    }

}
