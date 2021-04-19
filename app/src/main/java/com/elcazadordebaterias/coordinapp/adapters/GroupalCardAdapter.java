package com.elcazadordebaterias.coordinapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.GroupalCard;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class GroupalCardAdapter extends RecyclerView.Adapter<GroupalCardAdapter.GroupalCardViewHolder> {

    ArrayList<GroupalCard> groupsList;
    Context context;

    private OnItemClickListener listener;

    public GroupalCardAdapter(ArrayList<GroupalCard> groupsList, Context context){
        this.groupsList = groupsList;
        this.context = context;
    }

    @NonNull
    @Override
    public GroupalCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_groupalcard, parent, false);
        return new GroupalCardViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupalCardViewHolder holder, int position) {
        GroupalCard group = groupsList.get(position);

        holder.subjectImage.setImageResource(group.getSubjectImage());
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

    static class GroupalCardViewHolder extends RecyclerView.ViewHolder{
        ImageView subjectImage;
        TextView courseName;
        TextView subjectName;
        MaterialButton showParticipants;

        GroupalCardViewHolder(View view, OnItemClickListener listener){
            super(view);

            subjectImage = view.findViewById(R.id.subjectImage);
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
