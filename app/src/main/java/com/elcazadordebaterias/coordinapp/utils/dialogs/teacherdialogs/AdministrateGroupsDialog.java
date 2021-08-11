package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.listviews.SelectParticipantsListAdapter;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.SelectParticipantItem;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AdministrateGroupsDialog extends DialogFragment {

    private TextView textView1, textView2, textView3;
    private Spinner groupSpinner1, groupSpinner2;

    private MaterialButton interchangeButton;

    private ListView participantsGroup1ListView, participantsGroup2ListView;
    private ArrayList<SelectParticipantItem> participantsGroup1List, participantsGroup2List;
    private SelectParticipantsListAdapter group1ParticipantsAdapter, group2ParticipantsAdapter;

    private String selectedCourse;
    private String selectedSubject;

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private Context context;

    public AdministrateGroupsDialog(String selectedCourse, String selectedSubject) {
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        participantsGroup1List = new ArrayList<SelectParticipantItem>();
        participantsGroup2List = new ArrayList<SelectParticipantItem>();


    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context = context;

        group1ParticipantsAdapter = new SelectParticipantsListAdapter(context, participantsGroup1List);
        group2ParticipantsAdapter = new SelectParticipantsListAdapter(context, participantsGroup2List);
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.utils_dialogs_administrategroupsdialog, null);

        textView1 = view.findViewById(R.id.textView1);
        textView2 = view.findViewById(R.id.textView2);
        textView3 = view.findViewById(R.id.textView3);

        textView2.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);

        groupSpinner1 = view.findViewById(R.id.groupSpinner1);
        groupSpinner2 = view.findViewById(R.id.groupSpinner2);

        groupSpinner2.setVisibility(View.GONE);

        participantsGroup1ListView = view.findViewById(R.id.participantsGroup1ListView);
        participantsGroup2ListView = view.findViewById(R.id.participantsGroup2ListView);

        participantsGroup1ListView.setVisibility(View.GONE);
        participantsGroup2ListView.setVisibility(View.GONE);

        participantsGroup1ListView.setAdapter(group1ParticipantsAdapter);
        participantsGroup2ListView.setAdapter(group2ParticipantsAdapter);

        interchangeButton = view.findViewById(R.id.interchangeButton);
        interchangeButton.setVisibility(View.GONE);

        interchangeButton.setOnClickListener(v -> {

            ArrayList<SelectParticipantItem> updatedList1 = new ArrayList<SelectParticipantItem>();

            for (SelectParticipantItem selectedParticipantList1 : participantsGroup1List) { // Add non selected items from list 1
                if (!selectedParticipantList1.isSelected()) {
                    SelectParticipantItem newItem = new SelectParticipantItem(selectedParticipantList1.getParticipantName(), selectedParticipantList1.getParticipantId());
                    updatedList1.add(newItem);
                }
            }

            for (SelectParticipantItem selectedParticipantList2 : participantsGroup2List) { // Add selected items from list 2
                if (selectedParticipantList2.isSelected()) {
                    SelectParticipantItem newItem = new SelectParticipantItem(selectedParticipantList2.getParticipantName(), selectedParticipantList2.getParticipantId());
                    updatedList1.add(newItem);
                }
            }

            ArrayList<SelectParticipantItem> updatedList2 = new ArrayList<SelectParticipantItem>();

            for (SelectParticipantItem selectedParticipantList2 : participantsGroup2List) { // Add non selected items from list 2
                if (!selectedParticipantList2.isSelected()) {
                    SelectParticipantItem newItem = new SelectParticipantItem(selectedParticipantList2.getParticipantName(), selectedParticipantList2.getParticipantId());
                    updatedList2.add(newItem);
                }
            }

            for (SelectParticipantItem selectedParticipantList1 : participantsGroup1List) { // Add selected items from list 1
                if (selectedParticipantList1.isSelected()) {
                    SelectParticipantItem newItem = new SelectParticipantItem(selectedParticipantList1.getParticipantName(), selectedParticipantList1.getParticipantId());
                    updatedList2.add(newItem);
                }
            }

            if (updatedList1.size() < 2 || updatedList2.size() < 2) { // TODO: Check for more errors
                Toast.makeText(context, "Los grupos deben de tener al menos 2 personas", Toast.LENGTH_LONG).show();
            } else {
                participantsGroup1List.clear();
                participantsGroup1List.addAll(updatedList1);

                participantsGroup2List.clear();
                participantsGroup2List.addAll(updatedList2);

                group1ParticipantsAdapter.notifyDataSetChanged();
                group2ParticipantsAdapter.notifyDataSetChanged();
            }
        });

        ArrayList<String> groupSpinner1Names = new ArrayList<String>();
        ArrayList<String> groupSpinner2Names = new ArrayList<String>();

        // Course adapter
        ArrayAdapter<String> groups1ListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, groupSpinner1Names);
        groups1ListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner1.setAdapter(groups1ListAdapter);
        groupSpinner1.setSelection(0);

        CollectionReference groupCollRef = fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("CollectiveGroups");

        // Group 1 spinner
        groupCollRef
                .whereArrayContains("allParticipantsIDs", fAuth.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        GroupDocument group = document.toObject(GroupDocument.class);
                        groupSpinner1Names.add(group.getName());
                    }
                    groups1ListAdapter.notifyDataSetChanged();
                });

        // Group 2 spinner
        ArrayAdapter<String> groups2ListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, groupSpinner2Names);
        groups2ListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner2.setAdapter(groups2ListAdapter);
        groupSpinner2.setSelection(0);

        // Group 1 spinner listener
        groupSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                groupSpinner2Names.clear();
                String selectedGroupName = parent.getItemAtPosition(position).toString();
                groupCollRef
                        .whereArrayContains("allParticipantsIDs", fAuth.getUid())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                GroupDocument group = document.toObject(GroupDocument.class);
                                if (!selectedGroupName.equals(group.getName())) {
                                    groupSpinner2Names.add(group.getName());
                                }
                            }
                            groups2ListAdapter.notifyDataSetChanged();
                            textView2.setVisibility(View.VISIBLE);
                            groupSpinner2.setVisibility(View.VISIBLE);
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Group 2 spinner listener
        groupSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                groupCollRef
                        .whereEqualTo("name", (String) groupSpinner1.getSelectedItem())
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) { // TODO: It only can contain one group, since group names have to be unique
                                GroupDocument groupDocument = document.toObject(GroupDocument.class);
                                ArrayList<Group> groupsList = groupDocument.getGroups();
                                Group noTeacherGroup = null;

                                for (Group group : groupsList) {
                                    if (!group.getHasTeacher()) {
                                        noTeacherGroup = group;
                                    }
                                }

                                if (noTeacherGroup != null) {
                                    ArrayList<GroupParticipant> participants = noTeacherGroup.getParticipants();
                                    for (GroupParticipant participant : participants) {
                                        SelectParticipantItem participantItem = new SelectParticipantItem(participant.getParticipantFullName(), participant.getParticipantId());
                                        participantsGroup1List.add(participantItem);
                                    }
                                    group1ParticipantsAdapter.notifyDataSetChanged();
                                    textView3.setVisibility(View.VISIBLE);
                                    participantsGroup1ListView.setVisibility(View.VISIBLE);
                                    interchangeButton.setVisibility(View.VISIBLE);

                                    groupCollRef
                                            .whereEqualTo("name", (String) groupSpinner2.getSelectedItem())
                                            .get()
                                            .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                                for (QueryDocumentSnapshot document1 : queryDocumentSnapshots1) { // TODO: It only can contain one group, since group names have to be unique
                                                    GroupDocument groupDocument1 = document1.toObject(GroupDocument.class);
                                                    ArrayList<Group> groupsList1 = groupDocument1.getGroups();
                                                    Group noTeacherGroup1 = null;

                                                    for (Group group : groupsList1) {
                                                        if (!group.getHasTeacher()) {
                                                            noTeacherGroup1 = group;
                                                        }
                                                    }

                                                    if (noTeacherGroup1 != null) {
                                                        ArrayList<GroupParticipant> participants1 = noTeacherGroup1.getParticipants();
                                                        for (GroupParticipant participant : participants1) {
                                                            SelectParticipantItem participantItem = new SelectParticipantItem(participant.getParticipantFullName(), participant.getParticipantId());
                                                            participantsGroup2List.add(participantItem);
                                                        }
                                                        group2ParticipantsAdapter.notifyDataSetChanged();
                                                        participantsGroup2ListView.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        builder.setTitle("Administrador de grupos")
                .setView(view)
                .setNegativeButton("Cancelar", (dialog, i) -> {
                    // Just closes the dialog
                })
                .setPositiveButton("Modificar grupos", (dialog, i) -> {
                    String group1Name = (String) groupSpinner1.getSelectedItem();
                    String group2Name = (String) groupSpinner2.getSelectedItem();

                    DocumentReference subjectDocRef = fStore
                            .collection("CoursesOrganization")
                            .document(selectedCourse)
                            .collection("Subjects")
                            .document(selectedSubject);


                    // Reference to group 1
                    groupCollRef
                            .whereEqualTo("name", group1Name)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (QueryDocumentSnapshot group1Document : queryDocumentSnapshots) { // Should return only one document
                                    GroupDocument groupDocument = group1Document.toObject(GroupDocument.class);
                                    Group groupWithTeacher = null;
                                    Group groupWithoutTeacher = null;

                                    for (Group group : groupDocument.getGroups()) {
                                        if (group.getHasTeacher()) {
                                            groupWithTeacher = group;
                                        } else {
                                            groupWithoutTeacher = group;
                                        }
                                    }

                                    if (groupWithTeacher != null && groupWithoutTeacher != null) {
                                        ArrayList<Group> updatedGroups = new ArrayList<Group>();
                                        updatedGroups.add(groupWithTeacher);

                                        Group updatedGroupWithoutTeacher = new Group();

                                        updatedGroupWithoutTeacher.setName(groupWithoutTeacher.getName());
                                        updatedGroupWithoutTeacher.setCourseName(groupWithoutTeacher.getCourseName());
                                        updatedGroupWithoutTeacher.setSubjectName(groupWithoutTeacher.getSubjectName());
                                        updatedGroupWithoutTeacher.setHasTeacher(false);

                                        ArrayList<String> participantsIDs = new ArrayList<String>();
                                        ArrayList<GroupParticipant> participants = new ArrayList<GroupParticipant>();

                                        for (SelectParticipantItem item : participantsGroup1List) {
                                            participantsIDs.add(item.getParticipantId());
                                            participants.add(new GroupParticipant(item.getParticipantName(), item.getParticipantId()));
                                        }

                                        updatedGroupWithoutTeacher.setParticipantsIds(participantsIDs);
                                        updatedGroupWithoutTeacher.setParticipants(participants);
                                        updatedGroupWithoutTeacher.setCollectionId(groupWithoutTeacher.getCollectionId());

                                        updatedGroups.add(updatedGroupWithoutTeacher);

                                        subjectDocRef
                                                .get()
                                                .addOnSuccessListener(documentSnapshot1 -> {
                                                    Subject subject = documentSnapshot1.toObject(Subject.class);

                                                    ArrayList<String> updatedAllParticipantsIDs = new ArrayList<String>(participantsIDs);
                                                    updatedAllParticipantsIDs.add(subject.getTeacherID());

                                                    group1Document
                                                            .getReference()
                                                            .update("allParticipantsIDs", updatedAllParticipantsIDs)
                                                            .addOnSuccessListener(unused -> {
                                                                group1Document
                                                                        .getReference()
                                                                        .update("groups", updatedGroups)
                                                                        .addOnSuccessListener(unused1 -> {
// Reference to
                                                                        });
                                                            });
                                                });
                                    }
                                }
                            });
                });

        return builder.create();
    }

}
