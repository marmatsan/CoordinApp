package com.elcazadordebaterias.coordinapp.fragments.studentfragments.groups;

import android.content.Intent;
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
import com.elcazadordebaterias.coordinapp.activities.ChatActivity;
import com.elcazadordebaterias.coordinapp.utils.Group;
import com.elcazadordebaterias.coordinapp.utils.GroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * @see com.elcazadordebaterias.coordinapp.R.layout#fragment_groups_student_groupalchat
 */

public class GroupsFragment_GroupalChat extends Fragment {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups_student_groupalchat, container, false);

        // Recyclerview - Groups
        RecyclerView groupsPetitionsRecyclerView = v.findViewById(R.id.recyclerViewGroups);
        LinearLayoutManager groupsLayoutManager = new LinearLayoutManager(getContext());

        /*
        ArrayList<GroupCard> groupCardList = new ArrayList<GroupCard>();
        GroupalCardAdapter groupsAdapter = new GroupalCardAdapter(groupCardList, getContext());
        groupsAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            startActivity(intent);
        });

        groupsPetitionsRecyclerView.setAdapter(groupsAdapter);
        groupsPetitionsRecyclerView.setLayoutManager(groupsLayoutManager);

        // Create the groupal chat (only between students)
        fStore.collection("Groups").whereArrayContains("participantsIds", fAuth.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Group currentGroup = document.toObject(Group.class);
                    ArrayList<String> participantNames = new ArrayList<String>();

                    for(GroupParticipant participant : currentGroup.getParticipants()){
                        if(!participant.getParticipantAsTeacher()) { // The teacher is not displayed in the participants list, just the student.
                            participantNames.add(participant.getParticipantFullName());
                        }
                    }

                    GroupCard card = new GroupCard(document.getId(), currentGroup.getGroupName(), currentGroup.getSubjectName(), participantNames);
                    groupCardList.add(card);
                }
                groupsAdapter.notifyDataSetChanged();
            }
        });
        */

        return v;
    }
}
