package com.elcazadordebaterias.coordinapp.fragments.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.PetitionGroupCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.PetitionGroupCard;
import com.elcazadordebaterias.coordinapp.utils.PetitionGroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.PetitionRequest;
import com.elcazadordebaterias.coordinapp.utils.PetitionUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HomeFragment_Petitions extends Fragment {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_student_petitions, container, false);

        RecyclerView groupsPetitionsRecyclerView = v.findViewById(R.id.recyclerview_petitions);
        LinearLayoutManager petitionsLayoutManager = new LinearLayoutManager(getContext());

        ArrayList<PetitionGroupCard> petitions = new ArrayList<PetitionGroupCard>();

        PetitionGroupCardAdapter petitionsAdapter = new PetitionGroupCardAdapter(petitions, getContext());
        groupsPetitionsRecyclerView.setAdapter(petitionsAdapter);
        groupsPetitionsRecyclerView.setLayoutManager(petitionsLayoutManager);


        fStore.collection("Petitions").whereArrayContains("petitionUsersIds", fAuth.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    PetitionRequest currentPetition = document.toObject(PetitionRequest.class);
                    ArrayList<PetitionGroupParticipant> participantsList = new ArrayList<PetitionGroupParticipant>();

                    for(PetitionUser currentUser : currentPetition.getPetitionUsersList()){
                        participantsList.add(new PetitionGroupParticipant(currentUser.getUserFullName(), currentUser.getPetitionStatus()));
                    }

                    petitions.add(new PetitionGroupCard(document.getId(), currentPetition.getRequesterName(), currentPetition.getCourse() + " / " + currentPetition.getSubject(), participantsList));
                }
                petitionsAdapter.notifyDataSetChanged();
            }
        });

        return v;
    }

}
