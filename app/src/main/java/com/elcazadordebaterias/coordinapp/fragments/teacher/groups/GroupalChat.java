package com.elcazadordebaterias.coordinapp.fragments.teacher.groups;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.teachergroups.GroupTeacherCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.CourseParticipantCard;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.CollectiveGroupDocument;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class GroupalChat extends Fragment {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private ArrayList<GroupCard> groupsList;
    private GroupTeacherCardAdapter groupsAdapter;

    private int userType;

    private final String selectedCourse;
    private final String selectedSubject;

    private TextView noGroups;

    private TextInputLayout searchLayout;
    private EditText search;

    private HashMap<String, GroupCard> groupsListMap;

    private String teacherName = null;

    public GroupalChat(int userType, String selectedCourse, String selectedSubject) {
        this.userType = userType;
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        fStore
                .collection("Teachers")
                .document(fAuth.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    this.teacherName = (String) documentSnapshot.get("FullName");
                });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupsList = new ArrayList<GroupCard>();
        groupsAdapter = new GroupTeacherCardAdapter(groupsList, getContext());
        groupsListMap = new HashMap<String, GroupCard>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups_groupalchat, container, false);

        // Views
        RecyclerView recyclerviewGroups = view.findViewById(R.id.recyclerviewGroups);
        noGroups = view.findViewById(R.id.noGroups);
        searchLayout = view.findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);

        search = view.findViewById(R.id.searchText);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerviewGroups.setAdapter(groupsAdapter);
        recyclerviewGroups.setLayoutManager(layoutManager);

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

        collectiveGroupsCollRef
                .addSnapshotListener((queryDocumentsSnapshots, error) -> {

                    if (error != null) {
                        return;
                    } else if (queryDocumentsSnapshots == null) {
                        return;
                    }

                    groupsList.clear();
                    listChanged();

                    for (DocumentSnapshot groupDocument : queryDocumentsSnapshots) {
                        CollectiveGroupDocument group = groupDocument.toObject(CollectiveGroupDocument.class);

                        for (Group groupDoc : group.getGroups()) {
                            if (groupDoc.getHasTeacher()) {
                                GroupCard groupCard = new GroupCard(
                                        group.getName(),
                                        groupDocument.getId(),
                                        selectedCourse,
                                        selectedSubject,
                                        groupDoc.getHasTeacher(),
                                        groupDoc.getParticipantNames(),
                                        groupDoc.getCollectionId(),
                                        group.getSpokerID(),
                                        group.getSpokerName());
                                groupsList.add(groupCard);
                                groupsListMap.put(group.getName(), groupCard);
                            }
                        }

                        Collections.sort(groupsList, new Comparator<GroupCard>() {
                            @Override
                            public int compare(GroupCard groupCard1, GroupCard groupCard2) {
                                String groupName1 = groupCard1.getGroupName();
                                String groupName2 = groupCard2.getGroupName();

                                return extractInt(groupName1) - extractInt(groupName2);
                            }

                            int extractInt(String s) {
                                String num = s.replaceAll("\\D", "");
                                return num.isEmpty() ? 0 : Integer.parseInt(num);
                            }

                        });

                        listChanged();

                        groupDocument
                                .getReference()
                                .collection("ChatRoomWithoutTeacher")
                                .addSnapshotListener((chatDocumentsSnapshots, error2) -> {

                                    if (error2 != null) {
                                        return;
                                    } else if (chatDocumentsSnapshots == null) {
                                        return;
                                    }

                                    int numMessages = chatDocumentsSnapshots.size();
                                    GroupCard groupCard = groupsListMap.get(group.getName());

                                    if (groupCard != null) {
                                        groupCard.setNumMessages(numMessages);
                                    }

                                    groupsAdapter.notifyDataSetChanged();
                                });

                    }
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

    private void filter(String inputText) {
        if (inputText.isEmpty()) {
            groupsAdapter.filteredList(groupsList);
        } else {
            if (teacherName != null) {
                ArrayList<GroupCard> filteredList = new ArrayList<GroupCard>();
                for (GroupCard card : groupsList) {
                    if (card.getParticipantNames().contains(inputText) && !inputText.equalsIgnoreCase(teacherName)) {
                        filteredList.add(card);
                    }
                }
                groupsAdapter.filteredList(filteredList);
            }
        }
    }
}