package com.elcazadordebaterias.coordinapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.PetitionGroupCard;

import java.util.ArrayList;

public class PetitionGroupCardAdapter extends RecyclerView.Adapter<PetitionGroupCardAdapter.PetitionGroupCardViewHolder> {

    private ArrayList<PetitionGroupCard> petitionsList;

    public PetitionGroupCardAdapter(ArrayList<PetitionGroupCard> petitionsList){
        this.petitionsList = petitionsList;
    }

    @NonNull
    @Override
    public PetitionGroupCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_petitiongroupcard, parent, false);

        return new PetitionGroupCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetitionGroupCardViewHolder holder, int position) {
        PetitionGroupCard petitionCard = petitionsList.get(position);

        holder.requesterName.setText(petitionCard.getRequesterName());
        holder.courseName.setText(petitionCard.getCourseSubject());
        holder.participantsList = petitionCard.getParticipantsList();

    }

    @Override
    public int getItemCount() {
        return petitionsList.size();
    }

    static class PetitionGroupCardViewHolder extends RecyclerView.ViewHolder {
        TextView requesterName;
        TextView courseName;
        ListView participantsList;

        PetitionGroupCardViewHolder(View itemView) {
            super(itemView);

            requesterName = itemView.findViewById(R.id.requesterName);
            courseName = itemView.findViewById(R.id.courseName);
            participantsList = itemView.findViewById(R.id.participantsList);
        }
    }

}
