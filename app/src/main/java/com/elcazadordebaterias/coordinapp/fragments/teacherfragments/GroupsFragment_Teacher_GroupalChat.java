package com.elcazadordebaterias.coordinapp.fragments.teacherfragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.activities.ChatActivity;
import com.elcazadordebaterias.coordinapp.adapters.GroupalCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.GroupalCard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupsFragment_Teacher_GroupalChat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment_Teacher_GroupalChat extends Fragment {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups__teacher__groupalchat, container, false);

        // Recyclerview - Groups
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview_groupalchat);
        LinearLayoutManager groupsLayoutManager = new LinearLayoutManager(getContext());

        ArrayList<GroupalCard> groupCardList = new ArrayList<GroupalCard>();
        GroupalCardAdapter groupsAdapter = new GroupalCardAdapter(groupCardList, getContext());
        groupsAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            startActivity(intent);
        });

        return inflater.inflate(R.layout.fragment_groups__teacher__groupalchat, container, false);
    }
}