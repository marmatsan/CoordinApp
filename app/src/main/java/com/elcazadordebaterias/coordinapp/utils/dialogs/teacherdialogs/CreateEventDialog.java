package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.listviews.SelectGroupsItemAdapter;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.SelectGroupItem;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.CollectiveGroupDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.EventCardDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.MultichoiceCardDocument;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;

public class CreateEventDialog extends DialogFragment {

    // Selected course and subject
    private String selectedCourse;
    private String selectedSubject;

    // Firestore
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    private ArrayList<SelectGroupItem> groupItems;
    private SelectGroupsItemAdapter adapter;

    private Context context;

    private FloatingActionButton selectDate;

    public CreateEventDialog(String selectedCourse, String selectedSubject) {
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        groupItems = new ArrayList<SelectGroupItem>();
        adapter = new SelectGroupsItemAdapter(context, groupItems);
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.utils_dialogs_createventdialog, null);

        ListView groupList = view.findViewById(R.id.groupList);
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

        // Do something with this layouts?
        TextInputLayout eventTitleLayout = view.findViewById(R.id.eventTitleLayout);
        TextInputLayout eventDescriptionLayout = view.findViewById(R.id.eventDescriptionLayout);
        TextInputLayout eventPlaceLayout = view.findViewById(R.id.eventPlaceLayout);

        TextInputEditText eventTitle = view.findViewById(R.id.eventTitle);
        TextInputEditText eventDescription = view.findViewById(R.id.eventDescription);
        TextInputEditText eventPlace = view.findViewById(R.id.eventPlace);
        selectDate = view.findViewById(R.id.selectDate);

        selectDate.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Long> datePickerBuilder = MaterialDatePicker.Builder.datePicker();
            MaterialDatePicker<Long> picker = datePickerBuilder.build();
            picker.show(getParentFragmentManager(), picker.toString());
        });

        builder.setView(view)
                .setTitle("Crear evento")
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    // Just closes the dialog
                }).setPositiveButton("Crear evento", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {

            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener(view1 -> {
                String eventTitleText = eventTitle.getText().toString();
                String eventDescriptionText = eventDescription.getText().toString();
                String eventPlaceText = eventPlace.getText().toString();

                if (eventTitleText.isEmpty() || eventDescriptionText.isEmpty() || eventPlaceText.isEmpty()) {
                    Toast.makeText(context, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                } else {

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
                                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                        if (selectedGroupsIDs.contains(documentSnapshot.getId())) {
                                            EventCardDocument eventCardDocument = new EventCardDocument(eventTitleText, eventDescriptionText, eventPlaceText, true, fAuth.getUid());
                                            documentSnapshot.getReference().collection("TeachersEvents").add(eventCardDocument);
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
