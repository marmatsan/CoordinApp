package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.interactivity.teacher;

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
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.InteractivityCardsContainer;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GroupsInteractivityCardsContainerAdapter extends RecyclerView.Adapter<GroupsInteractivityCardsContainerAdapter.GroupsInteractivityCardsViewHolder> {

    private ArrayList<InteractivityCardsContainer> groupsList;
    private final RecyclerView.RecycledViewPool viewPool;
    private SparseBooleanArray expandState;
    private Context context;
    private QuerySnapshot queryDocumentSnapshots;

    public GroupsInteractivityCardsContainerAdapter(ArrayList<InteractivityCardsContainer> coursesList, Context context) {
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.utils_cards_interactivity_teachercards_interactivitycardscontainer, viewGroup, false);
        return new GroupsInteractivityCardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupsInteractivityCardsViewHolder holder, int position) {

        InteractivityCardsContainer groupsContainerCard = groupsList.get(position);

        holder.groupName.setText(groupsContainerCard.getName());
        holder.showInvisibleCards.setOnClickListener(view -> {
            if (queryDocumentSnapshots != null) {
                for (DocumentSnapshot interactivityDocument : queryDocumentSnapshots) {
                    Boolean isVisible = (Boolean) interactivityDocument.get("hasTeacherVisibility");
                    if (isVisible != null) {
                        if (!isVisible) {
                            interactivityDocument.getReference().update("hasTeacherVisibility", true);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });

        if (groupsContainerCard.getEvaluableGroupalTextCards() != 0) {
            holder.averageGroupalTextMark.setText(groupsContainerCard.getAverageGroupalTextMark());
        } else {
            holder.groupalStatistics.setVisibility(View.GONE);
        }


        if (groupsContainerCard.getInteractivityCardsList().size() == 0) {
            holder.informativeText.setVisibility(View.VISIBLE);
            String information = "No hay actividades que mostrar";
            holder.informativeText.setText(information);
            holder.expandActivitiesButton.setVisibility(View.GONE);
            holder.expandableView.setVisibility(View.GONE);
        } else {
            holder.informativeText.setVisibility(View.GONE);
            holder.expandActivitiesButton.setVisibility(View.VISIBLE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(holder.filesRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);

            layoutManager.setInitialPrefetchItemCount(groupsContainerCard.getInteractivityCardsList().size());
            InteractivityCardsAdapter interactivityCardsAdapter = new InteractivityCardsAdapter(groupsContainerCard.getInteractivityCardsList(), context);

            holder.filesRecyclerView.setLayoutManager(layoutManager);
            holder.filesRecyclerView.setAdapter(interactivityCardsAdapter);
            holder.filesRecyclerView.setRecycledViewPool(viewPool);

            final boolean isExpanded = expandState.get(position); //Check if the view is expanded
            holder.expandableView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            holder.expandActivitiesButton.setOnClickListener(view -> {
                if (holder.expandableView.getVisibility() == View.VISIBLE) {
                    holder.expandableView.setVisibility(View.GONE);
                    holder.expandActivitiesButton.setText(R.string.verActividades);
                    holder.expandActivitiesButton.setIconResource(R.drawable.ic_baseline_folder_open_24);
                    expandState.put(position, false);
                } else {
                    holder.expandableView.setVisibility(View.VISIBLE);
                    holder.expandActivitiesButton.setText(R.string.ocultarActividades);
                    holder.expandActivitiesButton.setIconResource(R.drawable.ic_baseline_folder_24);
                    expandState.put(position, true);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    static class GroupsInteractivityCardsViewHolder extends RecyclerView.ViewHolder {

        TextView groupName;
        TextView informativeText;

        ConstraintLayout groupalStatistics;
        TextView averageGroupalTextMark;

        MaterialButton expandActivitiesButton;
        MaterialButton showInvisibleCards;
        ConstraintLayout expandableView;
        RecyclerView filesRecyclerView;

        GroupsInteractivityCardsViewHolder(final View itemView) {
            super(itemView);

            groupName = itemView.findViewById(R.id.groupName);
            informativeText = itemView.findViewById(R.id.informativeText);

            groupalStatistics = itemView.findViewById(R.id.groupalStatistics);
            averageGroupalTextMark = itemView.findViewById(R.id.averageGroupalTextMark);



            expandActivitiesButton = itemView.findViewById(R.id.expandActivitiesButton);
            showInvisibleCards = itemView.findViewById(R.id.showInvisibleCards);
            expandableView = itemView.findViewById(R.id.expandableView);
            filesRecyclerView = itemView.findViewById(R.id.filesRecyclerView);
        }
    }

    public void setQueryDocumentSnapshots(QuerySnapshot queryDocumentSnapshots) {
        this.queryDocumentSnapshots = queryDocumentSnapshots;
    }

}
