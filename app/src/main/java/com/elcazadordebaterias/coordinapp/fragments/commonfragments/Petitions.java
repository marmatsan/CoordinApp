package com.elcazadordebaterias.coordinapp.fragments.commonfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.PetitionGroupCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionGroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.cards.PetitionGroupCard;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionRequest;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Petitions extends Fragment {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private String selectedCourse;
    private String selectedSubject;

    private final int userType;

    private ArrayList<PetitionGroupCard> petitionsList;
    private PetitionGroupCardAdapter petitionsAdapter;

    private TextView noPetitions;

    public Petitions(int userType, String selectedCourse, String selectedSubject){
        this.userType = userType;
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        petitionsList = new ArrayList<PetitionGroupCard>();
        petitionsAdapter = new PetitionGroupCardAdapter(petitionsList, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_commonfragments_petitions, container, false);

        // Recyclerview - Petitions
        RecyclerView petitionsContainer = view.findViewById(R.id.petitionsContainer);
        noPetitions = view.findViewById(R.id.noPetitions);

        petitionsContainer.setAdapter(petitionsAdapter);
        LinearLayoutManager petitionsLayoutManager = new LinearLayoutManager(getContext());
        petitionsContainer.setLayoutManager(petitionsLayoutManager);

        CollectionReference groupsCollRef = fStore
                .collection("Petitions");

        groupsCollRef
                .addSnapshotListener((value, error) -> {

                    if (error != null) {
                        return;
                    }

                    petitionsList.clear();

                    if (userType == UserType.TYPE_STUDENT) {
                        groupsCollRef.whereArrayContains("petitionUsersIds", fAuth.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> populatePetitions(queryDocumentSnapshots));
                    } else if (userType == UserType.TYPE_TEACHER){
                        groupsCollRef.whereEqualTo("teacherId", fAuth.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> populatePetitions(queryDocumentSnapshots));
                    }

                });

        return view;
    }

    private void populatePetitions(QuerySnapshot queryDocumentSnapshots){
        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
            PetitionRequest petition = document.toObject(PetitionRequest.class);
            ArrayList<PetitionGroupParticipant> participantsList = new ArrayList<PetitionGroupParticipant>();

            for (PetitionUser user : petition.getPetitionUsersList()){
                participantsList.add(new PetitionGroupParticipant(user.getUserFullName(), user.getPetitionStatus()));
            }
            petitionsList.add(new PetitionGroupCard(document.getId(), petition.getRequesterId(), petition.getRequesterName(), petition.getCourse() + " / " + petition.getSubject(), participantsList));
        }

        if (petitionsList.isEmpty()){
            noPetitions.setVisibility(View.VISIBLE);
        } else {
            noPetitions.setVisibility(View.GONE);
        }

        petitionsAdapter.notifyDataSetChanged();
    }

}