package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.interactivity.student;

import android.content.Context;
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
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards.GroupsInteractivityCardsContainer;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class GroupsInteractivityCardsAdapter extends RecyclerView.Adapter<GroupsInteractivityCardsAdapter.GroupsInteractivityCardsViewHolder> {

        private ArrayList<GroupsInteractivityCardsContainer> groupsList;
        private final RecyclerView.RecycledViewPool viewPool;
        private SparseBooleanArray expandState;
        private Context context;

        public GroupsInteractivityCardsAdapter(ArrayList<GroupsInteractivityCardsContainer> coursesList, Context context) {
            this.groupsList = coursesList;

            viewPool = new RecyclerView.RecycledViewPool();
            expandState = new SparseBooleanArray();

            for (int i = 0; i < coursesList.size(); i++) {
                expandState.append(i, false);
            }
            this.context = context;

        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @NonNull
        @Override
        public GroupsInteractivityCardsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.utils_cards_filescontainercard, viewGroup, false);

            return new GroupsInteractivityCardsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GroupsInteractivityCardsViewHolder viewHolder, int position) {

            GroupsInteractivityCardsContainer groupsContainerCard = groupsList.get(position);

            viewHolder.groupName.setText(groupsContainerCard.getName());

            LinearLayoutManager layoutManager = new LinearLayoutManager(viewHolder.filesRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);

            layoutManager.setInitialPrefetchItemCount(groupsContainerCard.getInteractivityCardsList().size());
            InteractivityCardsAdapter interactivityCardsAdapter = new InteractivityCardsAdapter(groupsContainerCard.getInteractivityCardsList(), context);

            viewHolder.filesRecyclerView.setLayoutManager(layoutManager);
            viewHolder.filesRecyclerView.setAdapter(interactivityCardsAdapter);
            viewHolder.filesRecyclerView.setRecycledViewPool(viewPool);

            final boolean isExpanded = expandState.get(position); //Check if the view is expanded
            viewHolder.expandableView.setVisibility(isExpanded?View.VISIBLE:View.GONE);

            viewHolder.expandFilesButton.setOnClickListener(view -> {
                if (viewHolder.expandableView.getVisibility() == View.VISIBLE){
                    viewHolder.expandableView.setVisibility(View.GONE);
                    viewHolder.expandFilesButton.setText(R.string.expandir);
                    expandState.put(position, false);
                }else{
                    viewHolder.expandableView.setVisibility(View.VISIBLE);
                    viewHolder.expandFilesButton.setText(R.string.colapsar);
                    expandState.put(position, true);
                }
            });

        }

        @Override
        public int getItemCount() {
            return groupsList.size();
        }

        static class GroupsInteractivityCardsViewHolder extends RecyclerView.ViewHolder {

            TextView groupName;
            MaterialButton expandFilesButton;
            ConstraintLayout expandableView;
            RecyclerView filesRecyclerView;

            GroupsInteractivityCardsViewHolder(final View itemView) {
                super(itemView);

                groupName = itemView.findViewById(R.id.groupName);
                expandFilesButton = itemView.findViewById(R.id.expandFilesButton);
                expandableView = itemView.findViewById(R.id.expandableView);
                filesRecyclerView = itemView.findViewById(R.id.filesRecyclerView);

            }
        }

}
