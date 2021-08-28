package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.listviews.SelectGroupsItemAdapter;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.SelectGroupItem;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.SelectParticipantItem;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.CollectiveGroupDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.InputTextCardDocument;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateInputTextCardDialog extends DialogFragment {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private final String selectedCourse;
    private final String selectedSubject;

    private ListView groupList;
    private TextInputLayout inputCardNameLayout;
    private TextInputEditText inputCardName;

    private CheckBox questionIsEvaluable;
    private CheckBox groupalQuestion;

    private ArrayList<SelectGroupItem> groupItems;
    private SelectGroupsItemAdapter adapter;

    private Context context;

    public CreateInputTextCardDialog(String selectedCourse, String selectedSubject) {
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        this.context = context;

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        groupItems = new ArrayList<SelectGroupItem>();
        adapter = new SelectGroupsItemAdapter(context, groupItems);

    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.utils_dialogs_createinputtextcarddialog, null);

        groupList = view.findViewById(R.id.groupList);
        inputCardNameLayout = view.findViewById(R.id.inputCardNameLayout);
        inputCardName = view.findViewById(R.id.inputCardName);
        questionIsEvaluable = view.findViewById(R.id.questionIsEvaluable);
        questionIsEvaluable.setActivated(false);
        groupalQuestion = view.findViewById(R.id.groupalQuestion);
        groupalQuestion.setActivated(false);

        groupList.setAdapter(adapter);

        // Collection reference
        CollectionReference collectiveGroupsCollRef = fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("CollectiveGroups");

        collectiveGroupsCollRef
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        CollectiveGroupDocument groupDocument = documentSnapshot.toObject(CollectiveGroupDocument.class);
                        String groupName = groupDocument.getName();
                        String groupID = documentSnapshot.getId();

                        SelectGroupItem groupItem = new SelectGroupItem(groupName, groupID);
                        groupItems.add(groupItem);
                    }
                    adapter.notifyDataSetChanged();
                });

        builder.setView(view)
                .setTitle("Crear nueva actividad de tipo entrada de texto")
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    // Just closes the dialog
                }).setPositiveButton("Crear", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {

            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener(view1 -> {
                String cardTitle = inputCardName.getText().toString();

                if (cardTitle.isEmpty()) {
                    inputCardNameLayout.setErrorEnabled(true);
                    inputCardNameLayout.setError("El título de la pregunta no puede estar vacío");
                } else {
                    inputCardNameLayout.setErrorEnabled(false);

                    ArrayList<String> selectedGroupsIDs = new ArrayList<String>();

                    for (SelectGroupItem item : groupItems) {
                        if (item.isSelected()) {
                            selectedGroupsIDs.add(item.getGroupID());
                        }
                    }

                    if (selectedGroupsIDs.isEmpty()) {
                        Toast.makeText(context, "Selecciona al menos un grupo", Toast.LENGTH_SHORT).show();
                    } else {

                        collectiveGroupsCollRef
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    for (DocumentSnapshot collectiveGroupDocumentSnapshot : queryDocumentSnapshots) {
                                        if (selectedGroupsIDs.contains(collectiveGroupDocumentSnapshot.getId())) {
                                            if (selectedGroupsIDs.contains(collectiveGroupDocumentSnapshot.getId())) {
                                                CollectiveGroupDocument collectiveGroupDocument = collectiveGroupDocumentSnapshot.toObject(CollectiveGroupDocument.class);

                                                if (groupalQuestion.isChecked()) {
                                                    ArrayList<String> studentsIDs = new ArrayList<String>();
                                                    studentsIDs.add(collectiveGroupDocument.getSpokerID());
                                                    InputTextCardDocument textCardDocument = new InputTextCardDocument(cardTitle, questionIsEvaluable.isChecked(), true, studentsIDs);
                                                    collectiveGroupDocumentSnapshot.getReference().collection("InteractivityCards").add(textCardDocument);
                                                } else {
                                                    ArrayList<Group> groups = collectiveGroupDocument.getGroups();

                                                    for (Group group : groups) {
                                                        if (!group.getHasTeacher()) {
                                                            ArrayList<String> studentsIDs = group.getParticipantsIds();
                                                            InputTextCardDocument textCardDocument = new InputTextCardDocument(cardTitle, questionIsEvaluable.isChecked(), false, studentsIDs);
                                                            collectiveGroupDocumentSnapshot.getReference().collection("InteractivityCards").add(textCardDocument);
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });
                        dialog.dismiss();
                    }
                }
            });
        });

        return dialog;
    }
}
