package com.elcazadordebaterias.coordinapp.adapters.recyclerviews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.dialogs.commondialogs.DisplayParticipantsListDialog;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionGroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.cards.PetitionGroupCard;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionRequest;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionUser;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Adapter to handle the list of {@link PetitionGroupCard}'s, which is used for both teacher and
 * student.
 *
 * @author Martín Mateos Sánchez
 */
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

        // Bind views of petitionCard
        if (petitionCard.getRequesterId().equals(fAuth.getUid())){
            holder.requesterName.setText(R.string.peticion_propia);
        } else {
            holder.requesterName.setText(petitionCard.getRequesterName());
        }

        holder.courseName.setText(petitionCard.getCourseSubject());

        holder.acceptRequest.setOnClickListener(v -> {
            fStore.collection("Petitions").document(petitionCard.getPetitionId()).get().addOnSuccessListener(documentSnapshot -> {
                PetitionRequest petition = documentSnapshot.toObject(PetitionRequest.class);

                if (petition.getTeacherId().equals(fAuth.getUid())) { // The logged user is a teacher
                    createNewGroup(petition, petitionCard, position);
                } else {
                    updatePetition(documentSnapshot, petitionCard, position, PetitionUser.STATUS_ACCEPTED);
                }

            });
        });

        holder.denyRequest.setOnClickListener(v -> {
            fStore.collection("Petitions").document(petitionCard.getPetitionId()).get().addOnSuccessListener(documentSnapshot -> {
                PetitionRequest petition = documentSnapshot.toObject(PetitionRequest.class);

                if (petition.getTeacherId().equals(fAuth.getUid())) { // The logged user is a teacher
                    fStore.collection("Petitions").document(documentSnapshot.getId()).delete();
                    petitionsList.remove(position);
                    notifyDataSetChanged();
                } else {
                    updatePetition(documentSnapshot, petitionCard, position, PetitionUser.STATUS_REJECTED);
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
        ArrayList<PetitionGroupParticipant> participantsList = new ArrayList<PetitionGroupParticipant>();

        for(PetitionUser user : petitionUsers){
            if(user.getUserId().equals(fAuth.getUid())){
                user.setPetitionStatus(newStatus);
            }
            participantsList.add(new PetitionGroupParticipant(user.getUserFullName(), user.getPetitionStatus()));
        }

        petitionCard.setParticipantsList(participantsList);

        fStore.collection("Petitions").document(document.getId())
                .update("petitionUsersList", petitionUsers)
                .addOnSuccessListener(aVoid -> notifyItemChanged(position));
    }

    // Creates the new group and deletes the petition that has been accepted
    private void createNewGroup(PetitionRequest petition, PetitionGroupCard petitionCard, int position){

        ArrayList<GroupParticipant> participants = new ArrayList<GroupParticipant>();

        for(PetitionUser user : petition.getPetitionUsersList()){
            participants.add(new GroupParticipant(user.getUserFullName(), user.getUserId()));
        }

        // Reference to the subject that the group is going to be created
        CollectionReference groupsCollRef =
                fStore.collection("CoursesOrganization")
                .document(petition.getCourse())
                .collection("Subjects")
                .document(petition.getSubject())
                .collection("Groups");

        // Search for the greatest group identifier
        groupsCollRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<String> groupsIdentifiers = new ArrayList<String>();

            for (DocumentSnapshot document : queryDocumentSnapshots){
                Group group = document.toObject(Group.class);
                groupsIdentifiers.add(group.getGroupName());
            }

            Group group = new Group(
                    petition.getTeacherId(),
                    petition.getTeacherName(),
                    petition.getCourse(),
                    petition.getSubject(),
                    petition.getPetitionUsersIds(),
                    participants);

            if (groupsIdentifiers.isEmpty()){ // There was no group in the collection
                group.setGroupName("Grupo 1");
            } else {
                ArrayList<Integer> numbers = new ArrayList<Integer>();

                for (String identifier : groupsIdentifiers){
                    String numberOnly = identifier.replaceAll("[^0-9]", "");
                    numbers.add(Integer.parseInt(numberOnly));
                }

                int maxNumber = Collections.max(numbers);
                int newGroupNumber = maxNumber + 1;

                String newGroupName = "Grupo " + newGroupNumber;

                group.setGroupName(newGroupName);

            }

            groupsCollRef
                    .add(group)
                    .addOnSuccessListener(documentReference -> fStore.collection("Petitions").document(petitionCard.getPetitionId()).delete());
            petitionsList.remove(position);
            notifyDataSetChanged();
        });


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
