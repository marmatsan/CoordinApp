package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class GroupCardAdapter extends RecyclerView.Adapter<GroupCardAdapter.GroupCardViewHolder> {

    ArrayList<GroupCard> groupsList;
    Context context;

    private OnItemClickListener listener;

    public GroupCardAdapter(ArrayList<GroupCard> groupsList, Context context){
        this.groupsList = groupsList;
        this.context = context;
    }

    @NonNull
    @Override
    public GroupCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_groupcard, parent, false);
        return new GroupCardViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupCardViewHolder holder, int position) {
        GroupCard group = groupsList.get(position);

        holder.courseName.setText(group.getCourseName());
        holder.subjectName.setText(group.getSubjectName());

        holder.showParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                builderSingle.setTitle("Participantes");

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.utils_participantname, R.id.participantName, group.getParticipantNames()){
                    @Override
                    public boolean isEnabled(int position) {
                        return false;
                    }
                };

                builderSingle.setNegativeButton("Vale", (dialog, which) -> dialog.dismiss());
                builderSingle.setAdapter(arrayAdapter, null);
                builderSingle.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    public interface OnItemClickListener{
        void onItemClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    static class GroupCardViewHolder extends RecyclerView.ViewHolder{
        TextView courseName;
        TextView subjectName;
        MaterialButton showParticipants;

        GroupCardViewHolder(View view, OnItemClickListener listener){
            super(view);

            courseName = view.findViewById(R.id.courseName);
            subjectName = view.findViewById(R.id.subjectName);
            showParticipants = view.findViewById(R.id.showParticipants);

            view.setOnClickListener(view1 -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClicked(position);
                    }
                }
            });
        }
    }
}

