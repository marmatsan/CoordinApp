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
import com.elcazadordebaterias.coordinapp.utils.cards.PetitionCard;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionRequest;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Petitions extends Fragment {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private String selectedCourse;
    private String selectedSubject;

    private final int userType;

    private ArrayList<PetitionCard> petitionsList;
    private PetitionGroupCardAdapter petitionsAdapter;

    private TextView noPetitions;

    public Petitions(int userType, String selectedCourse, String selectedSubject) {
        this.userType = userType;
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        petitionsList = new ArrayList<PetitionCard>();
        petitionsAdapter = new PetitionGroupCardAdapter(petitionsList, getContext(), userType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commonfragments_petitions, container, false);

        // Recyclerview - Petitions
        RecyclerView petitionsContainer = view.findViewById(R.id.petitionsContainer);
        noPetitions = view.findViewById(R.id.noPetitions);

        if (userType == UserType.TYPE_TEACHER) {
            noPetitions.setText(R.string.no_petition_teacher);
        } else {
            noPetitions.setText(R.string.por_ahora_no_tienes_ninguna_petici_n_puedes_crear_una_petici_n_para_formar_un_grupo_en_la_pesta_a_grupos);
        }

        petitionsContainer.setAdapter(petitionsAdapter);
        LinearLayoutManager petitionsLayoutManager = new LinearLayoutManager(getContext());
        petitionsContainer.setLayoutManager(petitionsLayoutManager);

        CollectionReference petitionsCollRef = fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("Petitions");

        petitionsCollRef
                .addSnapshotListener((value, error) -> {

                    if (error != null) {
                        return;
                    }

                    petitionsList.clear();

                    if (userType == UserType.TYPE_STUDENT) {
                        petitionsCollRef.whereArrayContains("petitionUsersIds", fAuth.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> populatePetitions(queryDocumentSnapshots));
                    } else if (userType == UserType.TYPE_TEACHER) {
                        petitionsCollRef.whereEqualTo("teacherId", fAuth.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> populatePetitions(queryDocumentSnapshots));
                    }

                });

        return view;
    }

    private void populatePetitions(QuerySnapshot queryDocumentSnapshots) {
        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
            PetitionRequest petition = document.toObject(PetitionRequest.class);
            ArrayList<PetitionGroupParticipant> participantsList = new ArrayList<PetitionGroupParticipant>();

            for (PetitionUser user : petition.getPetitionUsersList()) {
                participantsList.add(new PetitionGroupParticipant(user.getUserFullName(), user.getPetitionStatus()));
            }

            petitionsList.add(new PetitionCard(selectedCourse, selectedSubject, document.getId(), petition.getRequesterId(), petition.getRequesterName(), participantsList));
        }

        if (petitionsList.isEmpty()) {
            noPetitions.setVisibility(View.VISIBLE);
        } else {
            noPetitions.setVisibility(View.GONE);
        }

        petitionsAdapter.notifyDataSetChanged();
    }

}