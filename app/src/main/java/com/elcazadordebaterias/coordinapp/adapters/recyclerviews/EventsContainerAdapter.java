package com.elcazadordebaterias.coordinapp.adapters.recyclerviews;

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
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.interactivity.teacher.InteractivityCardsAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.EventCard;
import com.elcazadordebaterias.coordinapp.utils.cards.EventContainerCard;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class EventsContainerAdapter extends RecyclerView.Adapter<EventsContainerAdapter.EventsContainerViewHolder> {

    private ArrayList<EventContainerCard> eventContainerList;
    private final RecyclerView.RecycledViewPool viewPool;
    private SparseBooleanArray expandState;

    public EventsContainerAdapter(ArrayList<EventContainerCard> eventContainerList){
        this.eventContainerList = eventContainerList;

        viewPool = new RecyclerView.RecycledViewPool();
        expandState = new SparseBooleanArray();

    }

    @NonNull
    @Override
    public EventsContainerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_eventcardcontainer, parent, false);
        return new EventsContainerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsContainerViewHolder holder, int position) {
        EventContainerCard eventContainerCard = eventContainerList.get(position);

        holder.groupName.setText(eventContainerCard.getGroupName());

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.eventsContainer.getContext(), LinearLayoutManager.VERTICAL, false);

        layoutManager.setInitialPrefetchItemCount(eventContainerCard.getEventsList().size());
        EventsAdapter eventsAdapter = new EventsAdapter(eventContainerCard.getEventsList());

        holder.eventsContainer.setLayoutManager(layoutManager);
        holder.eventsContainer.setAdapter(eventsAdapter);
        holder.eventsContainer.setRecycledViewPool(viewPool);

        final boolean isExpanded = expandState.get(position); //Check if the view is expanded
        holder.expandableView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        holder.expandEventsButton.setOnClickListener(view -> {
            if (holder.expandableView.getVisibility() == View.VISIBLE) {
                holder.expandableView.setVisibility(View.GONE);
                holder.expandEventsButton.setText(R.string.verEventos);
                holder.expandEventsButton.setIconResource(R.drawable.ic_baseline_folder_open_24);
                expandState.put(position, false);
            } else {
                holder.expandableView.setVisibility(View.VISIBLE);
                holder.expandEventsButton.setText(R.string.ocultarEventos);
                holder.expandEventsButton.setIconResource(R.drawable.ic_baseline_folder_24);
                expandState.put(position, true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventContainerList.size();
    }


    public static class EventsContainerViewHolder extends RecyclerView.ViewHolder {

        TextView groupName;
        MaterialButton expandEventsButton;
        ConstraintLayout expandableView;
        RecyclerView eventsContainer;

        public EventsContainerViewHolder(@NonNull View itemView) {
            super(itemView);
            groupName = itemView.findViewById(R.id.groupName);
            expandEventsButton = itemView.findViewById(R.id.expandEventsButton);
            expandableView = itemView.findViewById(R.id.expandableView);
            eventsContainer = itemView.findViewById(R.id.eventsContainer);

        }
    }

}
