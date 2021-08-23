package com.elcazadordebaterias.coordinapp.fragments.teacher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.interactivity.student.GroupsInteractivityCardsAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards.GroupsInteractivityCardsContainer;
import com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs.CreateInputTextCardDialog;
import com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs.CreateMultichoiceCardDialog;
import com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs.CreateReminderCardDialog;
import com.elcazadordebaterias.coordinapp.utils.utilities.ButtonAnimator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Interactivity extends Fragment {

    // Firestore
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private String selectedCourse;
    private String selectedSubject;

    private TextView warningMessage;

    // Buttons
    FloatingActionButton createInteractivityCard;
    FloatingActionButton createInputTextCard;
    FloatingActionButton createMultichoiceCard;
    FloatingActionButton createReminderCard;

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
        View view = inflater.inflate(R.layout.fragment_teacher_interactivity, container, false);

        // Buttons
        createInteractivityCard = view.findViewById(R.id.createInteractivityCard);
        createInputTextCard = view.findViewById(R.id.createInputTextCard);
        createMultichoiceCard = view.findViewById(R.id.createMultichoiceCard);
        createReminderCard = view.findViewById(R.id.createReminderCard);

        // Warnings
        warningMessage = view.findViewById(R.id.warningMessage);
        warningMessage.setVisibility(View.GONE);

        // InteractivityCardsContainer
        RecyclerView interactivityCardsContainerRecyclerView = view.findViewById(R.id.interactivityCardsContainer);
        ArrayList<GroupsInteractivityCardsContainer> cardsList = new ArrayList<GroupsInteractivityCardsContainer>();
        GroupsInteractivityCardsAdapter adapter = new GroupsInteractivityCardsAdapter(cardsList, getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        interactivityCardsContainerRecyclerView.setAdapter(adapter);
        interactivityCardsContainerRecyclerView.setLayoutManager(layoutManager);

        fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("CollectiveGroups")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        warningMessage.setText(R.string.noGroups);
                        warningMessage.setVisibility(View.VISIBLE);
                        hideButtons();
                    } else {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            documentSnapshot
                                    .getReference()
                                    .collection("InteractivityCards")
                                    .get()
                                    .addOnSuccessListener(interactivityCardsDocumentSnapshots -> {
                                        if (interactivityCardsDocumentSnapshots.isEmpty()) {
                                            warningMessage.setText(R.string.noStatistics);
                                            warningMessage.setVisibility(View.VISIBLE);
                                        } else {
                                            for (DocumentSnapshot interactivityCardDocumentSnapshot : interactivityCardsDocumentSnapshots) {
                                                Long cardType = interactivityCardDocumentSnapshot.getLong("cardType");

                                                if (cardType != null) {
                                                    
                                                }

                                            }
                                            warningMessage.setVisibility(View.GONE);
                                        }
                                        showButtons();
                                    });
                        }
                    }
                });

        ArrayList<FloatingActionButton> buttons = new ArrayList<FloatingActionButton>();
        buttons.add(createInputTextCard);
        buttons.add(createMultichoiceCard);
        buttons.add(createReminderCard);

        ButtonAnimator buttonAnimator = new ButtonAnimator(getContext(), createInteractivityCard, buttons);

        // Buttons listeners
        createInteractivityCard.setOnClickListener(v -> buttonAnimator.onButtonClicked());

        createInputTextCard.setOnClickListener(v -> {
            CreateInputTextCardDialog dialog = new CreateInputTextCardDialog(selectedCourse, selectedSubject);
            dialog.show(getParentFragmentManager(), "dialog");
        });

        createMultichoiceCard.setOnClickListener(v -> {
            CreateMultichoiceCardDialog dialog = new CreateMultichoiceCardDialog();
            dialog.show(getParentFragmentManager(), "dialog");
        });

        createReminderCard.setOnClickListener(v -> {
            CreateReminderCardDialog dialog = new CreateReminderCardDialog();
            dialog.show(getParentFragmentManager(), "dialog");
        });

        return view;
    }

    private void hideButtons() {
        createInteractivityCard.setVisibility(View.GONE);
        createInputTextCard.setVisibility(View.GONE);
        createMultichoiceCard.setVisibility(View.GONE);
        createReminderCard.setVisibility(View.GONE);
    }

    private void showButtons() {
        createInteractivityCard.setVisibility(View.VISIBLE);
    }

}