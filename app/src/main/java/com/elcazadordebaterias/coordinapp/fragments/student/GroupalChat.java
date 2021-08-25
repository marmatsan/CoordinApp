package com.elcazadordebaterias.coordinapp.fragments.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.activities.ChatActivity;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.studentgroups.GroupsContainerCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupsContainerCard;
import com.elcazadordebaterias.coordinapp.utils.dialogs.studentdialogs.RequestGroupDialog;
import com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs.CreateGroupDialog;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.CollectiveGroupDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.IndividualGroupDocument;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

    private int userType;

    private final String selectedCourse;
    private final String selectedSubject;

    private FloatingActionButton requestGroup;
    private FloatingActionButton openIndividualChat;

    private TextView noGroups;

    public GroupalChat(int userType, String selectedCourse, String selectedSubject) {
        this.userType = userType;
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        groupsList = new ArrayList<GroupsContainerCard>();
        groupsAdapter = new GroupsContainerCardAdapter(groupsList, getContext(), userType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_groups_groupalchat, container, false);

        // Views
        RecyclerView recyclerviewGroups = view.findViewById(R.id.groupalFilesContainer);
        requestGroup = view.findViewById(R.id.requestGroup);
        openIndividualChat = view.findViewById(R.id.openIndividualChat);

        CollectionReference collectiveGroupsCollRef = fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("CollectiveGroups");

        CollectionReference individualGroupsCollRef = fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("IndividualGroups");

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
                        } else {
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
                        CollectiveGroupDocument group = groupDocument.toObject(CollectiveGroupDocument.class);

                        String groupName = group.getName();
                        ArrayList<GroupCard> groupList = new ArrayList<GroupCard>();

                        for (Group groupDoc : group.getGroups()) {
                            if (groupDoc.getParticipantsIds().contains(fAuth.getUid())) {
                                GroupCard groupCard = new GroupCard(
                                        groupDoc.getName(),
                                        groupDocument.getId(),
                                        selectedCourse,
                                        selectedSubject,
                                        groupDoc.getHasTeacher(),
                                        groupDoc.getParticipantNames(),
                                        groupDoc.getCollectionId());
                                groupList.add(groupCard);
                            }
                        }
                        groupsList.add(new GroupsContainerCard(groupName, groupList));
                    }
                    listChanged();
                });

        return view;
    }

    private void listChanged() {
        groupsAdapter.notifyDataSetChanged();

        if (groupsList.isEmpty()) {
            noGroups.setVisibility(View.VISIBLE);
        } else {
            noGroups.setVisibility(View.GONE);
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
        intent.putExtra("userType", userType);
        getContext().startActivity(intent);
    }

}