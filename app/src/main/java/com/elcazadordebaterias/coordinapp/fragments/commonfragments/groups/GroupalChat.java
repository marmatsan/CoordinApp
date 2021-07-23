package com.elcazadordebaterias.coordinapp.fragments.commonfragments.groups;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups.CourseCardAdapter;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups.GroupCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.CourseCard;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.CourseSubjectCard;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupParticipant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

        RecyclerView recyclerviewGroups = view.findViewById(R.id.recyclerviewGroups);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerviewGroups.setAdapter(groupsAdapter);
        recyclerviewGroups.setLayoutManager(layoutManager);

        CollectionReference groupsCollRef =
                fStore.collection("CoursesOrganization").document(selectedCourse)
                        .collection("Subjects").document(selectedSubject)
                        .collection("Groups");

        if (userType == UserType.TYPE_STUDENT) {
            groupsCollRef.whereEqualTo("courseName", "4ºESO B")
                    .get().addOnSuccessListener(queryDocumentSnapshots -> populateGroups(queryDocumentSnapshots));
        } else if (userType == UserType.TYPE_TEACHER) {
            groupsCollRef.whereEqualTo("courseName", "4ºESO B")
                    .get().addOnSuccessListener(queryDocumentSnapshots -> populateGroups(queryDocumentSnapshots));
        }

        return view;
    }

    private void populateGroups(QuerySnapshot queryDocumentSnapshots) {
        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
            Group group = document.toObject(Group.class);
            ArrayList<String> participantsNames = new ArrayList<String>();

            for (GroupParticipant participant : group.getParticipants()) {
                participantsNames.add(participant.getParticipantFullName());
            }

            groupsList.add(new GroupCard(group.getName(), document.getId(), group.getCourseName(), group.getSubjectName(), participantsNames));
        }
        groupsAdapter.notifyDataSetChanged();
    }

}