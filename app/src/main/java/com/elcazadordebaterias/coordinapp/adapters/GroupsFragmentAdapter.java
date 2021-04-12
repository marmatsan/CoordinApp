package com.elcazadordebaterias.coordinapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.Group;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class GroupsFragmentAdapter extends RecyclerView.Adapter<GroupsFragmentAdapter.GroupsFragmentViewHolder> {

    ArrayList<Group> groupsList;

    public GroupsFragmentAdapter(ArrayList<Group> groupsList){
        this.groupsList = groupsList;
    }

    @NonNull
    @Override
    public GroupsFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_groupalchatlayout, parent, false);
        return new GroupsFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsFragmentViewHolder holder, int position) {
        Group group = groupsList.get(position);



    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    static class GroupsFragmentViewHolder extends RecyclerView.ViewHolder{
        ImageView subjectImage;
        TextView courseName;
        TextView subjectName;
        MaterialButton showParticipants;

        GroupsFragmentViewHolder(View view){
            super(view);
            subjectImage = view.findViewById(R.id.subjectImage);
            courseName = view.findViewById(R.id.courseName);
            subjectName = view.findViewById(R.id.subjectName);
            showParticipants = view.findViewById(R.id.showParticipants);
        }
    }
}
