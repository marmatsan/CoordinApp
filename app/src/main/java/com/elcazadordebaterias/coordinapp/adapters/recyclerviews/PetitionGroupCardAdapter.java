package com.elcazadordebaterias.coordinapp.adapters.recyclerviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.elcazadordebaterias.coordinapp.utils.dialogs.commondialogs.DisplayParticipantsListDialog;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionGroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.cards.PetitionCard;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionRequest;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionUser;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Adapter to handle the list of {@link PetitionCard}'s, which is used for both teacher and
 * student.
 *
 * @author Martín Mateos Sánchez
 */
public class PetitionGroupCardAdapter extends RecyclerView.Adapter<PetitionGroupCardAdapter.PetitionGroupCardViewHolder> {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private ArrayList<PetitionCard> petitionsList;
    private Context context;

    private int userType;

    public PetitionGroupCardAdapter(ArrayList<PetitionCard> petitionsList, Context context, int userType) {
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        this.petitionsList = petitionsList;
        this.context = context;
        this.userType = userType;
    }

    @NonNull
    @Override
    public PetitionGroupCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_petitiongroupcard, parent, false);

        return new PetitionGroupCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetitionGroupCardViewHolder holder, int position) {
        PetitionCard petitionCard = petitionsList.get(position);

        CollectionReference petitionsCollRef = fStore
                .collection("CoursesOrganization")
                .document(petitionCard.getSelectedCourse())
                .collection("Subjects")
                .document(petitionCard.getSelectedSubject())
                .collection("Petitions");


        // Bind views of petitionCard
        if (petitionCard.getRequesterId().equals(fAuth.getUid())) {
            holder.requesterName.setText(R.string.peticion_propia);
        } else {
            holder.requesterName.setText(petitionCard.getRequesterName());
        }

        if (userType == UserType.TYPE_TEACHER || petitionCard.getRequesterId().equals(fAuth.getUid())) {
            if (petitionCard.getRequesterId().equals(fAuth.getUid())) {
                holder.acceptRequest.setVisibility(View.GONE);
            }
            holder.denyRequest.setText(R.string.eliminar_peticion);
        }

        holder.acceptRequest.setOnClickListener(v -> {
            petitionsCollRef
                    .document(petitionCard.getPetitionId())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (userType == UserType.TYPE_TEACHER) { // The logged user is a teacher
                            createNewGroup(petitionCard.getSelectedCourse(), petitionCard.getSelectedSubject(), documentSnapshot);
                        } else {
                            updatePetition(documentSnapshot, PetitionUser.STATUS_ACCEPTED);
                        }

                    });
        });

        holder.denyRequest.setOnClickListener(v -> {
            petitionsCollRef
                    .document(petitionCard.getPetitionId())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (userType == UserType.TYPE_TEACHER || petitionCard.getRequesterId().equals(fAuth.getUid())) { // The logged user is a teacher
                            documentSnapshot.getReference().delete();
                        } else {
                            updatePetition(documentSnapshot, PetitionUser.STATUS_REJECTED);
                        }
                    });
        });

        holder.displayParticipantsList.setOnClickListener(v -> {
            DisplayParticipantsListDialog dialog = new DisplayParticipantsListDialog(petitionCard.getParticipantsList());
            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "dialog");
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
    public void updatePetition(DocumentSnapshot documentSnapshot, int newStatus) {
        PetitionRequest petition = documentSnapshot.toObject(PetitionRequest.class);

        ArrayList<PetitionUser> petitionUsers = petition.getPetitionUsersList();

        for (PetitionUser user : petitionUsers) {
            if (user.getUserId().equals(fAuth.getUid())) {
                user.setPetitionStatus(newStatus);
            }
        }

        documentSnapshot.getReference().update("petitionUsersList", petitionUsers);
    }

    // Creates the new group and deletes the petition that has been accepted
    private void createNewGroup(String selectedCourse, String selectedSubject, DocumentSnapshot documentSnapshot) {
        PetitionRequest petition = documentSnapshot.toObject(PetitionRequest.class);

        CollectionReference groupsCollRef =
                fStore
                        .collection("CoursesOrganization")
                        .document(selectedCourse)
                        .collection("Subjects")
                        .document(selectedSubject)
                        .collection("CollectiveGroups");


        groupsCollRef
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int maxGroupIdentifier = Group.getMaxGroupIdentifier(queryDocumentSnapshots);
                    Group.createGroup(groupsCollRef, selectedCourse, selectedSubject, petition.getPetitionUsersIds(), maxGroupIdentifier + 1, context, documentSnapshot.getReference(), null);
                });

    }

    static class PetitionGroupCardViewHolder extends RecyclerView.ViewHolder {
        TextView requesterName;
        MaterialButton acceptRequest;
        MaterialButton denyRequest;
        MaterialButton displayParticipantsList;

        PetitionGroupCardViewHolder(View itemView) {
            super(itemView);

            requesterName = itemView.findViewById(R.id.requesterName);
            acceptRequest = itemView.findViewById(R.id.acceptRequest);
            denyRequest = itemView.findViewById(R.id.denyRequest);
            displayParticipantsList = itemView.findViewById(R.id.displayParticipantsList);

        }
    }
}
