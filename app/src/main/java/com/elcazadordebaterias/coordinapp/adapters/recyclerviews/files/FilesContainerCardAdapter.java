package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.files;

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
import com.elcazadordebaterias.coordinapp.utils.cards.files.FilesContainerCard;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class FilesContainerCardAdapter extends RecyclerView.Adapter<FilesContainerCardAdapter.FilesContainerCardViewHolder> {

    private ArrayList<FilesContainerCard> groupsList;
    private final RecyclerView.RecycledViewPool viewPool;
    private SparseBooleanArray expandState;

    public FilesContainerCardAdapter(ArrayList<FilesContainerCard> coursesList) {
        this.groupsList = coursesList;

        viewPool = new RecyclerView.RecycledViewPool();
        expandState = new SparseBooleanArray();

        for (int i = 0; i < coursesList.size(); i++) {
            expandState.append(i, false);
        }

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public FilesContainerCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.utils_cards_filescontainercard, viewGroup, false);

        return new FilesContainerCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesContainerCardViewHolder viewHolder, int position) {

        FilesContainerCard filesContainerCard = groupsList.get(position);

        viewHolder.groupName.setText(filesContainerCard.getGroupName());

        LinearLayoutManager layoutManager = new LinearLayoutManager(viewHolder.filesRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);

        layoutManager.setInitialPrefetchItemCount(filesContainerCard.getFilesList().size());
        FilesCardListAdapter filesListAdapter = new FilesCardListAdapter(filesContainerCard.getFilesList());

        viewHolder.filesRecyclerView.setLayoutManager(layoutManager);
        viewHolder.filesRecyclerView.setAdapter(filesListAdapter);
        viewHolder.filesRecyclerView.setRecycledViewPool(viewPool);

        final boolean isExpanded = expandState.get(position); //Check if the view is expanded
        viewHolder.expandableView.setVisibility(isExpanded?View.VISIBLE:View.GONE);

        viewHolder.expandableView.setOnClickListener(view -> {
            if (viewHolder.expandableView.getVisibility() == View.VISIBLE){
                viewHolder.expandableView.setVisibility(View.GONE);
                viewHolder.expandFilesList.setText(R.string.expandir);
                expandState.put(position, false);
            }else{
                viewHolder.expandableView.setVisibility(View.VISIBLE);
                viewHolder.expandFilesList.setText(R.string.colapsar);
                expandState.put(position, true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    static class FilesContainerCardViewHolder extends RecyclerView.ViewHolder {

        TextView groupName;
        MaterialButton expandFilesList;
        ConstraintLayout expandableView;
        RecyclerView filesRecyclerView;

        FilesContainerCardViewHolder(final View itemView) {
            super(itemView);

            groupName = itemView.findViewById(R.id.groupName);
            expandFilesList = itemView.findViewById(R.id.expandFilesList);
            expandableView = itemView.findViewById(R.id.expandableView);
            filesRecyclerView = itemView.findViewById(R.id.filesRecyclerView);

        }
    }
}
