package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;

import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.CollectiveGroupDocument;

import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.InputTextCardDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.MultichoiceCardDocument;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateMultichoiceCardDialog extends DialogFragment {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private final String selectedCourse;
    private final String selectedSubject;

    Spinner groupNamesSpinner;

    private TextInputLayout inputCardNameLayout;
    private TextInputEditText inputCardName;
    private TextInputLayout inputQuestionTitleLayout;
    private TextInputEditText inputQuestionTitle;

    private FloatingActionButton addOption;
    private CheckBox questionIsEvaluable;
    private CheckBox groupalQuestion;

    private LinearLayout questionsTitleContainer;
    private RadioGroup questionsRadioGroup;

    TextView textView5;
    TextView errorMessage;

    public CreateMultichoiceCardDialog(String selectedCourse, String selectedSubject) {
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.utils_dialogs_createmultichoicecarddialog, null);

        groupNamesSpinner = view.findViewById(R.id.groupNamesSpinner);
        inputCardNameLayout = view.findViewById(R.id.inputCardNameLayout);
        inputCardName = view.findViewById(R.id.inputCardName);
        inputQuestionTitleLayout = view.findViewById(R.id.inputQuestionTitleLayout);
        inputQuestionTitle = view.findViewById(R.id.inputQuestionTitle);
        addOption = view.findViewById(R.id.addOption);
        textView5 = view.findViewById(R.id.textView5);
        textView5.setVisibility(View.GONE);

        questionIsEvaluable = view.findViewById(R.id.questionIsEvaluable);
        questionIsEvaluable.setChecked(false);

        groupalQuestion = view.findViewById(R.id.groupalQuestion);
        groupalQuestion.setChecked(false);

        questionsTitleContainer = view.findViewById(R.id.questionsTitleContainer);
        questionsRadioGroup = view.findViewById(R.id.questionsRadioGroup);
        questionsRadioGroup.setVisibility(View.GONE);

        errorMessage = view.findViewById(R.id.errorMessage);
        errorMessage.setVisibility(View.GONE);

        HashMap<String, String> groupMap = new HashMap<String, String>();

        // Collection reference
        CollectionReference collectiveGroupsCollRef = fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("CollectiveGroups");

        // Group adapter
        ArrayList<String> groupNames = new ArrayList<String>();

        ArrayAdapter<String> groupsListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, groupNames);
        groupsListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupNamesSpinner.setAdapter(groupsListAdapter);
        groupNamesSpinner.setSelection(0);

        collectiveGroupsCollRef
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        CollectiveGroupDocument groupDocument = documentSnapshot.toObject(CollectiveGroupDocument.class);
                        String groupName = groupDocument.getName();
                        String groupID = documentSnapshot.getId();

                        groupMap.put(groupName, groupID);
                    }

                    groupNames.addAll(groupMap.keySet());
                    groupsListAdapter.notifyDataSetChanged();
                });

        questionIsEvaluable.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                questionsRadioGroup.setVisibility(View.VISIBLE);
                questionsTitleContainer.setVisibility(View.GONE);
            } else {
                questionsRadioGroup.setVisibility(View.GONE);
                questionsTitleContainer.setVisibility(View.VISIBLE);
            }

        });

        addOption.setOnClickListener(v -> {
            if (inputQuestionTitle.getText().toString().isEmpty()) {
                inputQuestionTitleLayout.setErrorEnabled(true);
                inputQuestionTitleLayout.setError("El título de la opción no puede estar vacío");
            } else {
                inputQuestionTitleLayout.setErrorEnabled(false);
                if (questionsRadioGroup.getChildCount() >= 5 || questionsTitleContainer.getChildCount() >= 5) {
                    inputQuestionTitleLayout.setErrorEnabled(true);
                    inputQuestionTitleLayout.setError("No puedes añadir más de 5 opciones");
                } else if (inputQuestionTitle.getText().toString().length() > 100) {
                    inputQuestionTitleLayout.setErrorEnabled(true);
                    inputQuestionTitleLayout.setError("Enunciado demasiado largo");
                } else {

                    textView5.setVisibility(View.VISIBLE);
                    errorMessage.setVisibility(View.GONE);
                    inputQuestionTitleLayout.setErrorEnabled(false);
                    String newOption = inputQuestionTitle.getText().toString();

                    RadioButton button = new RadioButton(questionsRadioGroup.getContext());
                    button.setText(newOption);
                    questionsRadioGroup.addView(button);

                    TextView textView = new TextView(questionsTitleContainer.getContext());
                    textView.setText(newOption);
                    textView.setTextColor(Color.rgb(0, 0, 0));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(8, 8, 8, 8);
                    textView.setLayoutParams(params);

                    questionsTitleContainer.addView(textView);
                    inputQuestionTitle.getText().clear();
                }
            }
        });

        builder.setTitle("Crear nueva actividad de tipo multirespuesta")
                .setView(view)
                .setNegativeButton("Cancelar", (dialog, i) -> {
                    // Just closes the dialog
                })
                .setPositiveButton("Crear", null);

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {

            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener(view1 -> {
                String cardTitle = inputCardName.getText().toString();

                if (cardTitle.isEmpty()) {
                    inputCardNameLayout.setErrorEnabled(true);
                    inputCardNameLayout.setError("El título de la pregunta no puede estar vacío");
                } else if (cardTitle.length() > 100) {
                    inputCardNameLayout.setErrorEnabled(true);
                    inputCardNameLayout.setError("Título demasiado largo");
                } else {
                    inputCardNameLayout.setErrorEnabled(false);

                    if (questionsRadioGroup.getChildCount() < 2 || questionsTitleContainer.getChildCount() < 2) {
                        errorMessage.setText(R.string.debes_agregar_al_menos_dos_opciones);
                        errorMessage.setVisibility(View.VISIBLE);
                    } else {
                        boolean evaluableQuestion = questionIsEvaluable.isChecked();

                        if (evaluableQuestion) {
                            int checkedRadioButtonID = questionsRadioGroup.getCheckedRadioButtonId();

                            if (checkedRadioButtonID == -1) {
                                errorMessage.setText(R.string.noSelected);
                                errorMessage.setVisibility(View.VISIBLE);
                            } else { // Create evaluable multichoicecardactivity

                                String groupID = groupMap.get(groupNamesSpinner.getSelectedItem());

                                DocumentReference groupDocumentRef = collectiveGroupsCollRef.document(groupID);

                                groupDocumentRef
                                        .get()
                                        .addOnSuccessListener(documentSnapshot -> {
                                            CollectiveGroupDocument groupDocument = documentSnapshot.toObject(CollectiveGroupDocument.class);

                                            if (groupalQuestion.isChecked()) {
                                                ArrayList<String> studentsIDs = new ArrayList<String>();
                                                ArrayList<MultichoiceCardDocument.Question> questionsList = new ArrayList<MultichoiceCardDocument.Question>();

                                                studentsIDs.add(groupDocument.getSpokesStudentID());

                                                for (int i = 0; i < questionsRadioGroup.getChildCount(); i++) {
                                                    RadioButton button = (RadioButton) questionsRadioGroup.getChildAt(i);
                                                    MultichoiceCardDocument.Question newQuestion = new MultichoiceCardDocument.Question(button.getText().toString(), i);

                                                    newQuestion.setHasCorrectAnswer(button.getId() == checkedRadioButtonID);

                                                    questionsList.add(newQuestion);
                                                }

                                                MultichoiceCardDocument multichoiceCardDocument = new MultichoiceCardDocument(cardTitle, true, true, questionsList, studentsIDs);
                                                groupDocumentRef.collection("InteractivityCards").add(multichoiceCardDocument);
                                            } else {
                                                ArrayList<Group> groups = groupDocument.getGroups();

                                                for (Group group : groups) {
                                                    if (!group.getHasTeacher()) {
                                                        ArrayList<String> studentsIDs = group.getParticipantsIds();

                                                        ArrayList<MultichoiceCardDocument.Question> questionsList = new ArrayList<MultichoiceCardDocument.Question>();

                                                        for (int i = 0; i < questionsRadioGroup.getChildCount(); i++) {
                                                            RadioButton button = (RadioButton) questionsRadioGroup.getChildAt(i);
                                                            MultichoiceCardDocument.Question newQuestion = new MultichoiceCardDocument.Question(button.getText().toString(), i);

                                                            newQuestion.setHasCorrectAnswer(button.getId() == checkedRadioButtonID);

                                                            questionsList.add(newQuestion);
                                                        }

                                                        MultichoiceCardDocument multichoiceCardDocument = new MultichoiceCardDocument(cardTitle, true, false, questionsList, studentsIDs);

                                                        groupDocumentRef.collection("InteractivityCards").add(multichoiceCardDocument);
                                                        break;
                                                    }
                                                }
                                            }
                                        });

                                dialog.dismiss();
                            }
                        } else { // Create non evaluable multichoicecardactivity

                            String groupID = groupMap.get(groupNamesSpinner.getSelectedItem());

                            DocumentReference groupDocumentRef = collectiveGroupsCollRef.document(groupID);

                            groupDocumentRef
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        CollectiveGroupDocument groupDocument = documentSnapshot.toObject(CollectiveGroupDocument.class);
                                        ArrayList<Group> groups = groupDocument.getGroups();

                                        for (Group group : groups) {
                                            if (!group.getHasTeacher()) {
                                                ArrayList<String> studentsIDs = group.getParticipantsIds();

                                                ArrayList<MultichoiceCardDocument.Question> questionsList = new ArrayList<MultichoiceCardDocument.Question>();

                                                for (int i = 0; i < questionsTitleContainer.getChildCount(); i++) {
                                                    TextView questionTitleView = (TextView) questionsRadioGroup.getChildAt(i);
                                                    MultichoiceCardDocument.Question newQuestion = new MultichoiceCardDocument.Question(questionTitleView.getText().toString(), i);

                                                    newQuestion.setHasCorrectAnswer(false);

                                                    questionsList.add(newQuestion);
                                                }

                                                MultichoiceCardDocument multichoiceCardDocument = new MultichoiceCardDocument(cardTitle, false, groupalQuestion.isChecked(), questionsList, studentsIDs);

                                                groupDocumentRef.collection("InteractivityCards").add(multichoiceCardDocument);
                                                break;
                                            }
                                        }

                                    });

                            dialog.dismiss();
                        }
                    }
                }
            });
        });


        return dialog;
    }

}
