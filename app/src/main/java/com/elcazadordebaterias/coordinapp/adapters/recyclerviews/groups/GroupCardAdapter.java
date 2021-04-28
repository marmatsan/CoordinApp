package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class GroupCardAdapter extends RecyclerView.Adapter<GroupCardAdapter.GroupCardViewHolder> {

    ArrayList<GroupCard> groupsList;
    Context context;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private OnItemClickListener listener;

    public GroupCardAdapter(ArrayList<GroupCard> groupsList, Context context){
        this.groupsList = groupsList;
        this.context = context;

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
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

        holder.showParticipants.setOnClickListener(v -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
            builderSingle.setTitle("Participantes");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.utils_participantname, R.id.participantName, group.getParticipantNames()){
                @Override
                public boolean isEnabled(int position1) {
                    return false;
                }
            };

            builderSingle.setNegativeButton("Vale", (dialog, which) -> dialog.dismiss());
            builderSingle.setAdapter(arrayAdapter, null);
            builderSingle.show();
        });

        holder.deleteGroup.setOnClickListener(v -> {
            fStore.collection("CoursesOrganization")
                    .document(group.getCourseName())
                    .collection("Subjects")
                    .document(group.getSubjectName())
                    .collection("Groups")
                    .document(group.getGroupId())
                    .delete().addOnSuccessListener(aVoid -> {

                groupsList.remove(position);
                notifyDataSetChanged();

            });
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
        MaterialButton deleteGroup;

        GroupCardViewHolder(View view, OnItemClickListener listener){
            super(view);

            courseName = view.findViewById(R.id.courseName);
            subjectName = view.findViewById(R.id.subjectName);
            showParticipants = view.findViewById(R.id.showParticipants);
            deleteGroup = view.findViewById(R.id.deleteGroup);

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

