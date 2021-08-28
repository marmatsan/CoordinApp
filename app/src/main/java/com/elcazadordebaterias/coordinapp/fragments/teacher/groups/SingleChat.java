package com.elcazadordebaterias.coordinapp.fragments.teacher.groups;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.IndividualGroupDocument;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SingleChat extends Fragment {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private ArrayList<GroupCard> groupsList;
    private GroupTeacherCardAdapter groupsAdapter;

    private final int userType;

    private final String selectedCourse;
    private final String selectedSubject;

    private TextView noGroups;

    private TextInputLayout searchLayout;
    private EditText search;


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
        groupsAdapter = new GroupTeacherCardAdapter(groupsList, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups_singlechat, container, false);

        // Views
        RecyclerView recyclerviewGroups = view.findViewById(R.id.recyclerviewGroups);
        noGroups = view.findViewById(R.id.noGroups);
        searchLayout = view.findViewById(R.id.searchLayout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerviewGroups.setAdapter(groupsAdapter);
        recyclerviewGroups.setLayoutManager(layoutManager);

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

                    for (DocumentSnapshot groupDoc : queryDocumentsSnapshots) {
                        IndividualGroupDocument groupDocument = groupDoc.toObject(IndividualGroupDocument.class);

                        if (groupDocument.getHasVisibility()) {
                            Group group = groupDocument.getGroup();

                            GroupCard groupCard = new GroupCard(
                                    group.getName(),
                                    groupDoc.getId(),
                                    selectedCourse,
                                    selectedSubject,
                                    group.getHasTeacher(),
                                    group.getParticipantNames(),
                                    group.getCollectionId()
                            );

                            groupsList.add(groupCard);
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
            fStore
                    .collection("Teachers")
                    .document(fAuth.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        String teacherName = (String) documentSnapshot.get("FullName");
                        if (teacherName != null) {
                            ArrayList<GroupCard> filteredList = new ArrayList<GroupCard>();
                            for (GroupCard card : groupsList) {
                                if (card.getParticipantNames().contains(inputText.toLowerCase()) && !inputText.equalsIgnoreCase(teacherName)) {
                                    filteredList.add(card);
                                }
                            }
                            groupsAdapter.filteredList(filteredList);
                        }
                    });
        }
    }

}