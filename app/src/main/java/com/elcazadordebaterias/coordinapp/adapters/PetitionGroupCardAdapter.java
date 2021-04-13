package com.elcazadordebaterias.coordinapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.PetitionGroupCardParticipant;
import com.elcazadordebaterias.coordinapp.utils.PetitionGroupCard;
import com.elcazadordebaterias.coordinapp.utils.PetitionRequest;
import com.elcazadordebaterias.coordinapp.utils.PetitionUser;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PetitionGroupCardAdapter extends RecyclerView.Adapter<PetitionGroupCardAdapter.PetitionGroupCardViewHolder> {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private ArrayList<PetitionGroupCard> petitionsList;
    private Context mContext;

    public PetitionGroupCardAdapter(ArrayList<PetitionGroupCard> petitionsList, Context context){
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

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

        holder.acceptRequest.setOnClickListener(v -> {
            fStore.collection("Petitions").document(petitionCard.getPetitionId()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        PetitionRequest currentPetition = document.toObject(PetitionRequest.class);

                        for (PetitionUser user : currentPetition.getPetitionUsersList()){
                            if(user.getUserId().equals(fAuth.getUid()) && user.getUserAsTeacher()){ // A teacher has accepted the petition
                                createNewGroup();
                                break;
                            }else if (user.getUserId().equals(fAuth.getUid())){ // An student has accepted the petition
                                updatePetition(document,petitionCard, position, 1);
                                break;
                            }
                        }
                    }
                }
            });
        });

        holder.denyRequest.setOnClickListener(v -> {
            fStore.collection("Petitions").document(petitionCard.getPetitionId()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        updatePetition(document, petitionCard, position, 2);
                    }
                }
            });
        });

        holder.displayParticipantsList.setOnClickListener(v -> {
            if(holder.participantsList.getVisibility() == View.GONE){
                holder.participantsList.setVisibility(View.VISIBLE);
            }else{
                holder.participantsList.setVisibility(View.GONE);
            }
        });

        // Add all the participants to the card
        holder.participantsList.removeAllViews();

        for (int i = 0; i < petitionCard.getParticipantsList().size(); i++) {
            PetitionGroupCardParticipant currentParticipant = petitionCard.getParticipantsList().get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.utils_groupparticipant, null);

            TextView participantName = view.findViewById(R.id.participantName);
            ImageView petitionStatusImage = view.findViewById(R.id.petitionStatusImage);

            participantName.setText(currentParticipant.getParticipantName());

            int petitionImage = currentParticipant.getPetitionStatusImage();

            if(petitionImage == 0){
                petitionStatusImage.setImageResource(R.drawable.petition_pending);
            }else if(petitionImage == 1){
                petitionStatusImage.setImageResource(R.drawable.petition_accepted);
            }else{
                petitionStatusImage.setImageResource(R.drawable.petition_rejected);
            }

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

    // Update the image of the status of the user.
    public void updatePetition(DocumentSnapshot document, PetitionGroupCard petitionCard, int position, int newStatus){
        PetitionRequest currentPetition = document.toObject(PetitionRequest.class);

        ArrayList<PetitionUser> petitionUsers = currentPetition.getPetitionUsersList();
        ArrayList<PetitionGroupCardParticipant> participantsList = new ArrayList<PetitionGroupCardParticipant>();

        for(PetitionUser user : petitionUsers){
            if(user.getUserId().equals(fAuth.getUid())){
                user.setPetitionStatus(newStatus);
            }
            participantsList.add(new PetitionGroupCardParticipant(user.getUserFullName(), user.getPetitionStatus()));
        }

        petitionCard.setParticipantsList(participantsList);

        fStore.collection("Petitions").document(document.getId()).update("petitionUsersList", petitionUsers).addOnSuccessListener(aVoid -> notifyItemChanged(position));
    }

    private void createNewGroup(){

    }

    static class PetitionGroupCardViewHolder extends RecyclerView.ViewHolder {
        TextView requesterName;
        TextView courseName;
        MaterialButton acceptRequest;
        MaterialButton denyRequest;
        LinearLayout participantsList;
        MaterialButton displayParticipantsList;

        PetitionGroupCardViewHolder(View itemView) {
            super(itemView);

            requesterName = itemView.findViewById(R.id.requesterName);
            courseName = itemView.findViewById(R.id.courseName);
            acceptRequest = itemView.findViewById(R.id.acceptRequest);
            denyRequest = itemView.findViewById(R.id.denyRequest);
            participantsList = itemView.findViewById(R.id.participantsList);
            displayParticipantsList = itemView.findViewById(R.id.displayParticipantsList);
        }
    }
}
