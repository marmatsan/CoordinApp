package com.elcazadordebaterias.coordinapp.fragments.teacher.groups;

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
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.GroupCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupDocument;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SingleChat extends Fragment {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private ArrayList<GroupCard> groupsList;
    private GroupCardAdapter groupsAdapter;

    private int userType;

    private String selectedCourse;
    private String selectedSubject;

    private TextView noGroups;

    public SingleChat(int userType, String selectedCourse, String selectedSubject) {
        this.userType = userType;
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        groupsList = new ArrayList<GroupCard>();
        groupsAdapter = new GroupCardAdapter(groupsList, getContext(), userType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups_singlechat, container, false);

        // Views
        RecyclerView recyclerviewGroups = view.findViewById(R.id.recyclerviewGroups);
        noGroups = view.findViewById(R.id.noGroups);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerviewGroups.setAdapter(groupsAdapter);
        recyclerviewGroups.setLayoutManager(layoutManager);

        fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("IndividualGroups")
                .whereArrayContains("allParticipantsIDs", fAuth.getUid())
                .addSnapshotListener((queryDocumentsSnapshots, error) -> {

                    if (error != null) {
                        return;
                    } else if (queryDocumentsSnapshots == null) {
                        return;
                    }

                    groupsList.clear();

                    for (DocumentSnapshot groupDocument : queryDocumentsSnapshots) {
                        GroupDocument group = groupDocument.toObject(GroupDocument.class);

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
                                groupsList.add(groupCard);
                            }
                        }

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