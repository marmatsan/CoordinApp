package com.elcazadordebaterias.coordinapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups.CourseCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.Group;
import com.elcazadordebaterias.coordinapp.utils.GroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.CourseCard;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.CourseSubjectCard;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupsFragment_GroupalChat} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment_GroupalChat extends Fragment {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    ArrayList<CourseCard> coursesList;
    CourseCardAdapter coursesAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        coursesList = new ArrayList<CourseCard>();
        coursesAdapter = new CourseCardAdapter(coursesList, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups_groupalchat, container, false);

        RecyclerView recyclerviewGroups = v.findViewById(R.id.recyclerviewGroups);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerviewGroups.setAdapter(coursesAdapter);
        recyclerviewGroups.setLayoutManager(layoutManager);

        return v;
    }

}