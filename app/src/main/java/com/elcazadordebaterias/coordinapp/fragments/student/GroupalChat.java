package com.elcazadordebaterias.coordinapp.fragments.student;

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
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.studentgroups.GroupsContainerCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupsContainerCard;
import com.elcazadordebaterias.coordinapp.utils.dialogs.commondialogs.CreateGroupDialog;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupDocument;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

        requestGroup.setOnClickListener(v -> {
            CreateGroupDialog dialog = new CreateGroupDialog(userType, selectedCourse, selectedSubject);
            dialog.show(getParentFragmentManager(), "dialog");
        });

        noGroups = view.findViewById(R.id.noGroups);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerviewGroups.setAdapter(groupsAdapter);
        recyclerviewGroups.setLayoutManager(layoutManager);

        fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("CollectiveGroups")
                .addSnapshotListener((queryDocumentsSnapshots, error) -> {

                    if (error != null) {
                        return;
                    } else if (queryDocumentsSnapshots == null) {
                        return;
                    }

                    groupsList.clear();

                    for (DocumentSnapshot groupDocument : queryDocumentsSnapshots) {
                        GroupDocument group = groupDocument.toObject(GroupDocument.class);

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

}