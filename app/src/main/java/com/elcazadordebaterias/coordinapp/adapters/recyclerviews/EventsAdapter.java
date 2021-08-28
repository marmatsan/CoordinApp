package com.elcazadordebaterias.coordinapp.adapters.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.cards.EventCard;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsCardViewHolder> {

    private ArrayList<EventCard> cardsList;

    public EventsAdapter(ArrayList<EventCard> cardsList){
        this.cardsList = cardsList;
    }

    @NonNull
    @Override
    public EventsCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_eventcard, parent, false);
        return new EventsCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsCardViewHolder holder, int position) {
        EventCard eventCard = cardsList.get(position);
    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }


    public static class EventsCardViewHolder extends RecyclerView.ViewHolder {

        TextView eventTitle;
        TextView eventMessage;
        TextView eventPlace;

        public EventsCardViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventMessage = itemView.findViewById(R.id.eventMessage);
            eventPlace = itemView.findViewById(R.id.eventPlace);

        }
    }

}
