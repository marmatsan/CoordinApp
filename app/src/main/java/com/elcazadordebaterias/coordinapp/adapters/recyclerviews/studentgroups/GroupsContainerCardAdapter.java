package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.studentgroups;

import android.app.AlertDialog;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.teachergroups.GroupTeacherCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupsContainerCard;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class GroupsContainerCardAdapter extends RecyclerView.Adapter<GroupsContainerCardAdapter.GroupsContainerCardViewHolder> {

    private ArrayList<GroupsContainerCard> groupsList;
    private final RecyclerView.RecycledViewPool viewPool;
    private SparseBooleanArray expandState;

    private int userType;

    private Context context;

    public GroupsContainerCardAdapter(ArrayList<GroupsContainerCard> coursesList, Context context, int userType) {
        this.groupsList = coursesList;

        viewPool = new RecyclerView.RecycledViewPool();
        expandState = new SparseBooleanArray();

        for (int i = 0; i < coursesList.size(); i++) {
            expandState.append(i, false);
        }
        this.userType = userType;
        this.context = context;

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public GroupsContainerCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.utils_cards_groupsstudentcontainercard, viewGroup, false);

        return new GroupsContainerCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsContainerCardViewHolder holder, int position) {

        GroupsContainerCard groupsContainerCard = groupsList.get(position);

        holder.groupName.setText(groupsContainerCard.getName());
        String spoker = "Portavoz: " + groupsContainerCard.getSpokerName();
        holder.spokerName.setText(spoker);

        holder.showParticipants.setOnClickListener(view -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
            builderSingle.setTitle("Participantes");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.utils_participantname, R.id.participantName, groupsContainerCard.getParticipantsNames()) {
                @Override
                public boolean isEnabled(int position1) {
                    return false;
                }
            };

            builderSingle.setNegativeButton("Vale", (dialog, which) -> dialog.dismiss());
            builderSingle.setAdapter(arrayAdapter, null);
            builderSingle.show();
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.groupsContainer.getContext(), LinearLayoutManager.VERTICAL, false);

        layoutManager.setInitialPrefetchItemCount(groupsContainerCard.getGroupList().size());
        GroupStudentCardAdapter groupTeacherCardAdapter = new GroupStudentCardAdapter(groupsContainerCard.getGroupList(), context, userType);

        holder.groupsContainer.setLayoutManager(layoutManager);
        holder.groupsContainer.setAdapter(groupTeacherCardAdapter);
        holder.groupsContainer.setRecycledViewPool(viewPool);

        final boolean isExpanded = expandState.get(position); //Check if the view is expanded
        holder.expandableView.setVisibility(isExpanded?View.VISIBLE:View.GONE);

        holder.openChats.setOnClickListener(view -> {
            if (holder.expandableView.getVisibility() == View.VISIBLE){
                holder.expandableView.setVisibility(View.GONE);
                holder.openChats.setIconResource(R.drawable.ic_baseline_folder_open_24);
                holder.openChats.setText(R.string.abrir_grupos_de_chat);
                expandState.put(position, false);
            }else{
                holder.expandableView.setVisibility(View.VISIBLE);
                holder.openChats.setIconResource(R.drawable.ic_baseline_folder_24);
                holder.openChats.setText(R.string.cerrar_grupos_de_chat);
                expandState.put(position, true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    static class GroupsContainerCardViewHolder extends RecyclerView.ViewHolder {

        TextView groupName;
        TextView spokerName;
        MaterialButton showParticipants;
        MaterialButton openChats;
        ConstraintLayout expandableView;
        RecyclerView groupsContainer;

        GroupsContainerCardViewHolder(final View itemView) {
            super(itemView);

            groupName = itemView.findViewById(R.id.groupName);
            spokerName = itemView.findViewById(R.id.spokerName);
            showParticipants = itemView.findViewById(R.id.showParticipants);
            openChats = itemView.findViewById(R.id.openChats);
            expandableView = itemView.findViewById(R.id.expandableView);
            groupsContainer = itemView.findViewById(R.id.groupsContainer);
        }
    }
}
