package com.elcazadordebaterias.coordinapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.DisplayParticipantsListDialog;
import com.elcazadordebaterias.coordinapp.utils.Group;
import com.elcazadordebaterias.coordinapp.utils.GroupParticipant;
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
                                createNewGroup(currentPetition, petitionCard, position);
                                break;
                            }else if (user.getUserId().equals(fAuth.getUid())){ // An student has accepted the petition
                                updatePetition(document, petitionCard, position, 1);
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
                        PetitionRequest currentPetition = document.toObject(PetitionRequest.class);

                        for (PetitionUser user : currentPetition.getPetitionUsersList()){
                            if(user.getUserId().equals(fAuth.getUid()) && user.getUserAsTeacher()){ // A teacher has deny the petition. Delete the petition
                                fStore.collection("Petitions").document(document.getId()).delete();

                                petitionsList.remove(position);
                                notifyItemChanged(position);
                                break;
                            }else if (user.getUserId().equals(fAuth.getUid())){ // An student has deny the petition

                                updatePetition(document, petitionCard, position, 2);
                                break;
                            }
                        }
                    }
                }
            });
        });

        holder.displayParticipantsList.setOnClickListener(v -> {
            DisplayParticipantsListDialog dialog = new DisplayParticipantsListDialog(petitionCard.getParticipantsList());
            dialog.show(((AppCompatActivity)mContext).getSupportFragmentManager(), "dialog");
        });

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

    // Creates the new group and deletes the petition that has been accepted
    private void createNewGroup(PetitionRequest currentPetition, PetitionGroupCard petitionCard, int position){

        ArrayList<GroupParticipant> participants = new ArrayList<GroupParticipant>();

        for(PetitionUser user : currentPetition.getPetitionUsersList()){
            participants.add(new GroupParticipant(user.getUserFullName(), user.getUserAsTeacher(), user.getUserId()));
        }

        Group group = new Group(fAuth.getUid(),
                                currentPetition.getCourse(),
                                currentPetition.getSubject(),
                                currentPetition.getPetitionUsersIds(),
                                participants);


        fStore.collection("Groups").document(fAuth.getUid()).set(group).addOnSuccessListener(documentReference -> fStore.collection("Petitions").document(petitionCard.getPetitionId()).delete());
        petitionsList.remove(position);
        notifyItemChanged(position);

    }

    static class PetitionGroupCardViewHolder extends RecyclerView.ViewHolder {
        TextView requesterName;
        TextView courseName;
        MaterialButton acceptRequest;
        MaterialButton denyRequest;
        MaterialButton displayParticipantsList;

        PetitionGroupCardViewHolder(View itemView) {
            super(itemView);

            requesterName = itemView.findViewById(R.id.requesterName);
            courseName = itemView.findViewById(R.id.courseName);
            acceptRequest = itemView.findViewById(R.id.acceptRequest);
            denyRequest = itemView.findViewById(R.id.denyRequest);
            displayParticipantsList = itemView.findViewById(R.id.displayParticipantsList);

        }
    }
}
