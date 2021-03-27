package com.elcazadordebaterias.coordinapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.GroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.PetitionGroupCard;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class PetitionGroupCardAdapter extends RecyclerView.Adapter<PetitionGroupCardAdapter.PetitionGroupCardViewHolder> {

    private ArrayList<PetitionGroupCard> petitionsList;
    private Context mContext;

    public PetitionGroupCardAdapter(ArrayList<PetitionGroupCard> petitionsList, Context context){
        this.petitionsList = petitionsList;
        this.mContext = context;
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
        holder.participantsList.setVisibility(View.GONE);

        holder.displayParticipantsList.setOnClickListener(v -> {
            if(holder.participantsList.getVisibility() == View.GONE){
                holder.participantsList.setVisibility(View.VISIBLE);
            }else{
                holder.participantsList.setVisibility(View.GONE);
            }
        });

        for (int i = 0; i < petitionCard.getParticipantsList().size(); i++) {
            GroupParticipant currentParticipant = petitionCard.getParticipantsList().get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.utils_groupparticipant, null);

            TextView participantName = view.findViewById(R.id.participantName);
            ImageView petitionStatusImage = view.findViewById(R.id.petitionStatusImage);

            participantName.setText(currentParticipant.getParticipantName());
            petitionStatusImage.setImageResource(currentParticipant.getPetitionStatusImage());

            holder.participantsList.addView(view);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return petitionsList.size();
    }

    static class PetitionGroupCardViewHolder extends RecyclerView.ViewHolder {
        TextView requesterName;
        TextView courseName;
        MaterialButton displayParticipantsList;
        LinearLayout participantsList;

        PetitionGroupCardViewHolder(View itemView) {
            super(itemView);

            requesterName = itemView.findViewById(R.id.requesterName);
            courseName = itemView.findViewById(R.id.courseName);
            displayParticipantsList = itemView.findViewById(R.id.displayParticipantsList);
            participantsList = itemView.findViewById(R.id.participantsList);

        }
    }
}
