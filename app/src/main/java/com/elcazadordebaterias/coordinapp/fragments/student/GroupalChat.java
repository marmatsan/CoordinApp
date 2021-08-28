package com.elcazadordebaterias.coordinapp.fragments.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.activities.ChatActivity;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.studentgroups.GroupsContainerCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupsContainerCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.elcazadordebaterias.coordinapp.utils.dialogs.studentdialogs.CreateEventDialog;
import com.elcazadordebaterias.coordinapp.utils.dialogs.studentdialogs.RequestGroupDialog;
import com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs.CreateGroupDialog;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.CollectiveGroupDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.IndividualGroupDocument;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupalChat} factory method to
 * create an instance of this fragment.
 */
public class GroupalChat extends Fragment {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private ArrayList<GroupsContainerCard> groupsList;
    private GroupsContainerCardAdapter groupsAdapter;


    private final String selectedCourse;
    private final String selectedSubject;

    private FloatingActionButton requestGroup;
    private FloatingActionButton openIndividualChat;

    private TextView noGroups;

    private CollectionReference individualGroupsCollRef;

    private TextInputLayout searchLayout;
    private EditText search;

    public GroupalChat(String selectedCourse, String selectedSubject) {
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        groupsList = new ArrayList<GroupsContainerCard>();
        groupsAdapter = new GroupsContainerCardAdapter(groupsList, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_groups_groupalchat, container, false);

        // Views
        RecyclerView recyclerviewGroups = view.findViewById(R.id.groupalFilesContainer);
        requestGroup = view.findViewById(R.id.requestGroup);
        openIndividualChat = view.findViewById(R.id.openIndividualChat);

        noGroups = view.findViewById(R.id.noGroups);
        searchLayout = view.findViewById(R.id.searchLayout);

        search = view.findViewById(R.id.searchText);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        CollectionReference collectiveGroupsCollRef = fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("CollectiveGroups");

        individualGroupsCollRef = fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("IndividualGroups");

        FloatingActionButton createEvent = view.findViewById(R.id.createEvent);

        createEvent.setOnClickListener(view1 -> {
            fStore
                    .collection("CoursesOrganization")
                    .document(selectedCourse)
                    .collection("Subjects")
                    .document(selectedSubject)
                    .collection("CollectiveGroups")
                    .whereArrayContains("allParticipantsIDs", fAuth.getUid())
                    .whereEqualTo("spokerID", fAuth.getUid())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (queryDocumentSnapshots.isEmpty()) {
                            Context context = getContext();
                            if (context != null) {
                                Toast.makeText(getContext(), "Para enviar eventos tienes que ser el portavoz de al menos un grupo", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            CreateEventDialog dialog = new CreateEventDialog(selectedCourse, selectedSubject);
                            dialog.show(getParentFragmentManager(), "dialog");
                        }
                    });
        });

        requestGroup.setOnClickListener(v -> {
            RequestGroupDialog dialog = new RequestGroupDialog(selectedCourse, selectedSubject);
            dialog.show(getParentFragmentManager(), "dialog");
        });

        openIndividualChat.setOnClickListener(v -> {
            individualGroupsCollRef
                    .document(fAuth.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            openChat(documentSnapshot);
                        } else { // Create the individualChat and open it
                            createIndividualChat();
                        }
                    });
        });

        noGroups = view.findViewById(R.id.noGroups);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerviewGroups.setAdapter(groupsAdapter);
        recyclerviewGroups.setLayoutManager(layoutManager);

        collectiveGroupsCollRef
                .whereArrayContains("allParticipantsIDs", fAuth.getUid())
                .addSnapshotListener((queryDocumentsSnapshots, error) -> {

                    if (error != null) {
                        return;
                    } else if (queryDocumentsSnapshots == null) {
                        return;
                    }

                    groupsList.clear();

                    for (DocumentSnapshot groupDocument : queryDocumentsSnapshots) {
                        CollectiveGroupDocument collectiveGroupDocument = groupDocument.toObject(CollectiveGroupDocument.class);

                        String groupName = collectiveGroupDocument.getName();
                        String spokerName = collectiveGroupDocument.getSpokerName();
                        String spokerID = collectiveGroupDocument.getSpokerID();

                        ArrayList<GroupCard> groupList = new ArrayList<GroupCard>();
                        ArrayList<String> participantsNames = new ArrayList<String>();

                        for (Group groupDoc : collectiveGroupDocument.getGroups()) {
                            GroupCard groupCard = new GroupCard(
                                    groupDoc.getName(),
                                    groupDocument.getId(),
                                    selectedCourse,
                                    selectedSubject,
                                    groupDoc.getHasTeacher(),
                                    groupDoc.getParticipantNames(),
                                    groupDoc.getCollectionId(),
                                    collectiveGroupDocument.getSpokerID(),
                                    collectiveGroupDocument.getSpokerName());
                            groupList.add(groupCard);
                            Log.d("DEBUGGING", "" + participantsNames);

                            if (groupDoc.getHasTeacher()) {
                                participantsNames.addAll(groupDoc.getParticipantNames());
                            }
                        }
                        Log.d("DEBUGGING", "" + participantsNames);
                        groupsList.add(new GroupsContainerCard(groupName, spokerName, spokerID, participantsNames, groupList));
                    }
                    listChanged();
                });

        return view;
    }

    private void listChanged() {
        groupsAdapter.notifyDataSetChanged();

        if (groupsList.isEmpty()) {
            noGroups.setVisibility(View.VISIBLE);
            searchLayout.setVisibility(View.GONE);
        } else {
            noGroups.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);
        }
    }

    private void openChat(DocumentSnapshot documentSnapshot) {
        IndividualGroupDocument groupDocument = documentSnapshot.toObject(IndividualGroupDocument.class);
        Group group = groupDocument.getGroup();

        GroupCard groupCard = new GroupCard(
                group.getName(),
                documentSnapshot.getId(),
                selectedCourse,
                selectedSubject,
                group.getHasTeacher(),
                group.getParticipantNames(),
                group.getCollectionId()
        );

        Intent intent = new Intent(getContext(), ChatActivity.class);

        // Convert the GroupCard to JSON to send it to ChatActivity
        Gson gson = new Gson();
        String cardAsString = gson.toJson(groupCard);
        intent.putExtra("cardAsString", cardAsString);
        intent.putExtra("userType", UserType.TYPE_STUDENT);
        getContext().startActivity(intent);
    }

    private void filter(String inputText) {

        if (inputText.isEmpty()) {
            groupsAdapter.filteredList(groupsList);
        } else {
            fStore
                    .collection("Teachers")
                    .document(fAuth.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        String teacherName = (String) documentSnapshot.get("FullName");
                        if (teacherName != null) {
                            ArrayList<GroupsContainerCard> filteredList = new ArrayList<GroupsContainerCard>();
                            for (GroupsContainerCard card : groupsList) {
                                if (card.getParticipantsNames().contains(inputText.toLowerCase()) && !inputText.equalsIgnoreCase(teacherName)) {
                                    filteredList.add(card);
                                }
                            }
                            groupsAdapter.filteredList(filteredList);
                        }
                    });
        }
    }

    private void createIndividualChat() {
        fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .get()
                .addOnSuccessListener(documentSnapshot12 -> {
                    String teacherID = (String) documentSnapshot12.get("teacherID");

                    fStore
                            .collection("Teachers")
                            .document(teacherID)
                            .get()
                            .addOnSuccessListener(documentSnapshot13 -> {
                                String teacherName = (String) documentSnapshot13.get("FullName");

                                fStore
                                        .collection("Students")
                                        .document(fAuth.getUid())
                                        .get()
                                        .addOnSuccessListener(documentSnapshot1 -> {
                                            String studentName = (String) documentSnapshot1.get("FullName");

                                            ArrayList<String> participantsIDs = new ArrayList<String>();
                                            ArrayList<GroupParticipant> participants = new ArrayList<GroupParticipant>();

                                            GroupParticipant teacher = new GroupParticipant(teacherName, teacherID);
                                            GroupParticipant student = new GroupParticipant(studentName, fAuth.getUid());

                                            participants.add(teacher);
                                            participants.add(student);

                                            participantsIDs.add(teacherID);
                                            participantsIDs.add(fAuth.getUid());


                                            Group group = new Group(
                                                    "Chat con " + studentName,
                                                    selectedCourse,
                                                    selectedSubject,
                                                    true,
                                                    participantsIDs,
                                                    participants,
                                                    "IndividualGroups"
                                            );

                                            IndividualGroupDocument individualGroup = new IndividualGroupDocument(group.getName(), participantsIDs, group);
                                            individualGroupsCollRef
                                                    .document(fAuth.getUid())
                                                    .set(individualGroup)
                                                    .addOnSuccessListener(unused -> {
                                                        individualGroupsCollRef
                                                                .document(fAuth.getUid())
                                                                .get()
                                                                .addOnSuccessListener(documentSnapshot2 -> {
                                                                    openChat(documentSnapshot2);
                                                                });
                                                    });

                                        });

                            });

                });
    }

}