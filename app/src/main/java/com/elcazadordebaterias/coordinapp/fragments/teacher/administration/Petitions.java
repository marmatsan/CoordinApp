package com.elcazadordebaterias.coordinapp.fragments.teacher.administration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.PetitionGroupCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionGroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.cards.PetitionGroupCard;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionRequest;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class Petitions extends Fragment {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private String selectedCourse;
    private String selectedSubject;

    public Petitions(String selectedCourse, String selectedSubject){
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_administration_teacher_petitions, container, false);

        // Recyclerview - Petitions
        RecyclerView groupsPetitionsRecyclerView = v.findViewById(R.id.recyclerview_petitions);
        LinearLayoutManager petitionsLayoutManager = new LinearLayoutManager(getContext());

        ArrayList<PetitionGroupCard> petitions = new ArrayList<PetitionGroupCard>();

        PetitionGroupCardAdapter petitionsAdapter = new PetitionGroupCardAdapter(petitions, getContext());
        groupsPetitionsRecyclerView.setAdapter(petitionsAdapter);
        groupsPetitionsRecyclerView.setLayoutManager(petitionsLayoutManager);

        fStore.collection("Petitions").whereEqualTo("teacherId", fAuth.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    PetitionRequest petition = document.toObject(PetitionRequest.class);
                    ArrayList<PetitionGroupParticipant> participantsList = new ArrayList<PetitionGroupParticipant>();

                    for(PetitionUser user : petition.getPetitionUsersList()){
                        participantsList.add(new PetitionGroupParticipant(user.getUserFullName(), user.getPetitionStatus()));
                    }
                    petitions.add(new PetitionGroupCard(document.getId(), petition.getRequesterId(), petition.getRequesterName(), petition.getCourse() + " / " + petition.getSubject(), participantsList));
                }
                petitionsAdapter.notifyDataSetChanged();
            }
        });

        return v;
    }

}