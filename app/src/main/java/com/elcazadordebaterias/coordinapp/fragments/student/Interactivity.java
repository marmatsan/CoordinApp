package com.elcazadordebaterias.coordinapp.fragments.student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.interactivity.GroupsInteractivityCardsAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.GroupsInteractivityCardsContainer;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.InputTextCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.InteractivityCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.InteractivityCardType;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.CollectiveGroupDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.InputTextCardDocument;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Interactivity extends Fragment {

    // Firestore
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private String selectedCourse;
    private String selectedSubject;

    public Interactivity(String selectedCourse, String selectedSubject) {
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_interactivity, container, false);

        // Container for the interactivity cards
        RecyclerView interactivityCardsContainerRecyclerView = view.findViewById(R.id.interactivityCardsContainerRecyclerView);

        ArrayList<GroupsInteractivityCardsContainer> cardsList = new ArrayList<GroupsInteractivityCardsContainer>();
        GroupsInteractivityCardsAdapter adapter = new GroupsInteractivityCardsAdapter(cardsList, getContext());

        fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("CollectiveGroups")
                .get()
                .addOnSuccessListener(collectiveGroupsDocumentSnapshots -> {
                    for (DocumentSnapshot collectiveGroupDocumentSnapshot : collectiveGroupsDocumentSnapshots) {
                        CollectiveGroupDocument collectiveGroupDocument = collectiveGroupDocumentSnapshot.toObject(CollectiveGroupDocument.class);

                        String groupName = collectiveGroupDocument.getName();

                        CollectionReference interactivityCardsCollRef =
                                collectiveGroupDocumentSnapshot
                                        .getReference()
                                        .collection("InteractivityCards");

                        interactivityCardsCollRef
                                .addSnapshotListener((interactivityCardsDocumentSnapshots, error) -> {

                                    if (error != null) {
                                        return;
                                    } else if (interactivityCardsDocumentSnapshots == null) {
                                        return;
                                    }

                                    cardsList.clear();
                                    ArrayList<InteractivityCard> interactivityCardsList = new ArrayList<InteractivityCard>();

                                    for (DocumentSnapshot interactivityCardDocumentSnapshot : interactivityCardsDocumentSnapshots) {

                                        Long cardType = interactivityCardDocumentSnapshot.getLong("cardType");

                                        if (cardType != null) {
                                            switch (cardType.intValue()) {
                                                case InteractivityCardType.TYPE_INPUTTEXT:
                                                    InputTextCardDocument inputTextCardDocument = interactivityCardDocumentSnapshot.toObject(InputTextCardDocument.class);

                                                    for (InputTextCardDocument.InputTextCardStudentData studentData : inputTextCardDocument.getStudentsData()) {
                                                        if (studentData.getStudentID().equals(fAuth.getUid()) && !studentData.getHasResponded()) {
                                                            InputTextCard inputTextCard = new InputTextCard(inputTextCardDocument.getTitle(), studentData.getStudentID(), interactivityCardDocumentSnapshot);
                                                            interactivityCardsList.add(inputTextCard);
                                                        }
                                                    }

                                                case InteractivityCardType.TYPE_CHOICES:

                                                default: // Reminder

                                            }
                                            GroupsInteractivityCardsContainer interactivityCardsContainer = new GroupsInteractivityCardsContainer(groupName, interactivityCardsList);
                                            cardsList.add(interactivityCardsContainer);
                                        }
                                    }

                                    adapter.notifyDataSetChanged();
                                });

                    }
                });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        interactivityCardsContainerRecyclerView.setAdapter(adapter);
        interactivityCardsContainerRecyclerView.setLayoutManager(layoutManager);

        adapter.notifyDataSetChanged();

        return view;
    }
}