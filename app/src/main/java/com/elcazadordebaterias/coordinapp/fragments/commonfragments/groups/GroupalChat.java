package com.elcazadordebaterias.coordinapp.fragments.commonfragments.groups;

import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
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
import com.elcazadordebaterias.coordinapp.utils.cards.GroupCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupParticipant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupalChat} factory method to
 * create an instance of this fragment.
 */
public class GroupalChat extends Fragment {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private ArrayList<GroupCard> groupsList;
    private GroupCardAdapter groupsAdapter;

    private int userType;

    private String selectedCourse;
    private String selectedSubject;

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

        groupsList = new ArrayList<GroupCard>();
        groupsAdapter = new GroupCardAdapter(groupsList, getContext(), userType);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups_groupalchat, container, false);

        // Views
        RecyclerView recyclerviewGroups = view.findViewById(R.id.recyclerviewGroups);
        noGroups = view.findViewById(R.id.noGroups);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerviewGroups.setAdapter(groupsAdapter);
        recyclerviewGroups.setLayoutManager(layoutManager);

        CollectionReference collectionRef = fStore
                .collection("CoursesOrganization").document(selectedCourse)
                .collection("Subjects").document(selectedSubject)
                .collection("CollectiveGroups");

        collectionRef
                .whereArrayContains("allParticipantsIDs", fAuth.getUid())
                .addSnapshotListener((queryDocumentsSnapshots, error) -> {

                    if (error != null) {
                        return;
                    } else if (queryDocumentsSnapshots == null) {
                        return;
                    }
                    Log.d("DEBUGGING", "Retrieved documents: "+queryDocumentsSnapshots.size());
                    Log.d("DEBUGGING", "Size before cleaning: "+groupsList.size());
                    groupsList.clear();
                    Log.d("DEBUGGING", "Size after cleaning: "+groupsList.size());

                    // Each of the groups the user is in
                    for (QueryDocumentSnapshot groupDocument : queryDocumentsSnapshots) {
                        if (userType == UserType.TYPE_TEACHER) {
                            groupDocument.getReference().collection("Groups")
                                    .whereArrayContains("participantsIds", fAuth.getUid())
                                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                Log.d("DEBUGGING", "Size of groups the user is in: "+queryDocumentSnapshots.size());

                                // Each of the groups inside the "Groups" collection (with and without teacher). For the teacher, this query should only return one document.
                                for (QueryDocumentSnapshot individualGroupDocument : queryDocumentSnapshots) {
                                    Group group = individualGroupDocument.toObject(Group.class);
                                    ArrayList<String> participantsNames = new ArrayList<String>();

                                    for (GroupParticipant participant : group.getParticipants()) {
                                        participantsNames.add(participant.getParticipantFullName());
                                    }

                                    groupsList.add(new GroupCard(
                                            group.getName(),
                                            groupDocument.getId(),
                                            group.getCourseName(),
                                            group.getSubjectName(),
                                            participantsNames,
                                            group.getCollectionId()));
                                }
                                Log.d("DEBUGGING", "EVENT");
                                groupsAdapter.notifyDataSetChanged();

                                if (groupsList.isEmpty()){
                                    noGroups.setVisibility(View.VISIBLE);
                                } else {
                                    noGroups.setVisibility(View.GONE);
                                }

                            });
                        }
                    }
                });
        return view;
    }

}