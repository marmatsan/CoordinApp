package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.studentgroups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;

import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GroupStudentCardAdapter extends RecyclerView.Adapter<GroupStudentCardAdapter.GroupStudentCardViewHolder> {

    ArrayList<GroupCard> groupsList;
    Context context;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private final int userType;

    public GroupStudentCardAdapter(ArrayList<GroupCard> groupsList, Context context, int userType) {
        this.groupsList = groupsList;
        this.context = context;
        this.userType = userType;

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public GroupStudentCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_studentgroupcard, parent, false);
        return new GroupStudentCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupStudentCardViewHolder holder, int position) {
        GroupCard groupCard = groupsList.get(position);

    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    static class GroupStudentCardViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;


        GroupStudentCardViewHolder(View view) {
            super(view);
            groupName = view.findViewById(R.id.groupName);
        }
    }

}

