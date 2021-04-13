package com.elcazadordebaterias.coordinapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.GroupalCardAdapter;
import com.elcazadordebaterias.coordinapp.adapters.PetitionGroupCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.GroupalCard;
import com.elcazadordebaterias.coordinapp.utils.PetitionGroupCard;

import java.util.ArrayList;

/**
 *
 * @see com.elcazadordebaterias.coordinapp.R.layout#fragment_groups_student_groupalchat
 */

public class GroupsFragment_Student_GroupalChat extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups_student_groupalchat, container, false);

        // Recyclerview - Groups
        RecyclerView groupsPetitionsRecyclerView = v.findViewById(R.id.recyclerViewGroups);
        LinearLayoutManager groupsLayoutManager = new LinearLayoutManager(getContext());

        ArrayList<GroupalCard> groups = new ArrayList<GroupalCard>();
        ArrayList<String> participantes = new ArrayList<String>();

        for (int i = 0; i < 20; i++){
            participantes.add("Participante");
        }

        groups.add(new GroupalCard("aaa", R.drawable.ic_baseline_maths_24, "3º ESO", "Matemáticas", participantes));

        GroupalCardAdapter groupsAdapter = new GroupalCardAdapter(groups, getContext());

        groupsPetitionsRecyclerView.setAdapter(groupsAdapter);
        groupsPetitionsRecyclerView.setLayoutManager(groupsLayoutManager);

        return v;
    }
}
