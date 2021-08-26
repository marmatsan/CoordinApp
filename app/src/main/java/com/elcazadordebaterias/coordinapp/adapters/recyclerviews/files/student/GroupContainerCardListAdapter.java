package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.files.student;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.files.FilesContainerCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.files.student.GroupContainerCard;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class GroupContainerCardListAdapter extends RecyclerView.Adapter<GroupContainerCardListAdapter.GroupContainerCardViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private ArrayList<GroupContainerCard> groupsContainerList;

    private Context context;

    public GroupContainerCardListAdapter(ArrayList<GroupContainerCard> groupsContainerList, Context context) {
        this.groupsContainerList = groupsContainerList;
        for (int i = 0; i < groupsContainerList.size(); i++) {
            expandState.append(i, false);
        }
        this.context = context;
    }

    @NonNull
    @Override
    public GroupContainerCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.utils_cards_files_student_groupcontainercard, viewGroup, false);

        return new GroupContainerCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupContainerCardViewHolder viewHolder, int position) {

        GroupContainerCard groupContainerCard = groupsContainerList.get(position);

        viewHolder.groupName.setText(groupContainerCard.getName());

        LinearLayoutManager layoutManager = new LinearLayoutManager(viewHolder.filesRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);

        layoutManager.setInitialPrefetchItemCount(groupContainerCard.getFilesContainerList().size());

        FilesContainerCardAdapter filesContainerAdapter = new FilesContainerCardAdapter(groupContainerCard.getFilesContainerList(), context);
        viewHolder.filesRecyclerView.setLayoutManager(layoutManager);
        viewHolder.filesRecyclerView.setAdapter(filesContainerAdapter);
        viewHolder.filesRecyclerView.setRecycledViewPool(viewPool);

        final boolean isExpanded = expandState.get(position); //Check if the view is expanded
        viewHolder.expandableView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        viewHolder.expandFilesButton.setOnClickListener(view -> {
            if (viewHolder.expandableView.getVisibility() == View.VISIBLE) {
                viewHolder.expandableView.setVisibility(View.GONE);
                viewHolder.expandFilesButton.setIconResource(R.drawable.ic_baseline_folder_open_24);
                viewHolder.expandFilesButton.setText(R.string.abrir_grupos_de_archivos);
                expandState.put(position, false);
            } else {
                viewHolder.expandableView.setVisibility(View.VISIBLE);
                viewHolder.expandFilesButton.setIconResource(R.drawable.ic_baseline_folder_24);
                viewHolder.expandFilesButton.setText(R.string.cerrar_grupos_de_archivos);
                expandState.put(position, true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupsContainerList.size();
    }

    static class GroupContainerCardViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        MaterialButton expandFilesButton;
        ConstraintLayout expandableView;
        RecyclerView filesRecyclerView;

        GroupContainerCardViewHolder(final View itemView) {
            super(itemView);

            groupName = itemView.findViewById(R.id.groupName);
            expandFilesButton = itemView.findViewById(R.id.expandFilesButton);
            expandableView = itemView.findViewById(R.id.expandableView);
            filesRecyclerView = itemView.findViewById(R.id.filesRecyclerView);

        }
    }
}
