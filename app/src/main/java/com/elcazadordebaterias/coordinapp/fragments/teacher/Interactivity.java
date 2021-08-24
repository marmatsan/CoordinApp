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
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.interactivity.teacher.GroupsInteractivityCardsAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.GroupsInteractivityCardsContainer;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.InputTextCardParent;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.InteractivityCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.InteractivityCardType;
import com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs.CreateInputTextCardDialog;
import com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs.CreateMultichoiceCardDialog;
import com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs.CreateReminderCardDialog;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.CollectiveGroupDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.InputTextCardDocument;
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
                .addSnapshotListener((collectiveGroupsDocumentSnapshots, error) -> {

                    if (error != null) {
                        return;
                    } else if (collectiveGroupsDocumentSnapshots == null) {
                        return;
                    }

                    if (collectiveGroupsDocumentSnapshots.isEmpty()) {
                        hideButtons();
                        warningMessage.setText(R.string.noGroups);
                        warningMessage.setVisibility(View.VISIBLE);
                    } else {
                        showButtons();
                        for (DocumentSnapshot documentSnapshot : collectiveGroupsDocumentSnapshots) {
                            String groupName = documentSnapshot.toObject(CollectiveGroupDocument.class).getName();
                            warningMessage.setVisibility(View.GONE);

                            documentSnapshot
                                    .getReference()
                                    .collection("InteractivityCards")
                                    .addSnapshotListener((interactivityCardsDocumentSnapshots, error1) -> {

                                        if (error1 != null) {
                                            return;
                                        } else if (interactivityCardsDocumentSnapshots == null) {
                                            return;
                                        }

                                        cardsList.clear();
                                        if (interactivityCardsDocumentSnapshots.isEmpty()) {
                                            warningMessage.setText(R.string.noStatistics);
                                            warningMessage.setVisibility(View.VISIBLE);
                                        } else {
                                            ArrayList<InteractivityCard> interactivityCardsList = new ArrayList<InteractivityCard>();

                                            for (DocumentSnapshot interactivityCardDocumentSnapshot : interactivityCardsDocumentSnapshots) {
                                                Long cardType = interactivityCardDocumentSnapshot.getLong("cardType");

                                                if (cardType != null) {
                                                    switch (cardType.intValue()) {
                                                        case InteractivityCardType.TYPE_INPUTTEXT:
                                                            InputTextCardDocument inputTextCardDocument = interactivityCardDocumentSnapshot.toObject(InputTextCardDocument.class);

                                                            ArrayList<InputTextCardParent.InputTextCardChild> inputTextCardChildList = new ArrayList<InputTextCardParent.InputTextCardChild>();
                                                            InputTextCardParent newCardParent = new InputTextCardParent(inputTextCardDocument.getTitle(), inputTextCardChildList, interactivityCardDocumentSnapshot);

                                                            float totalGrade = 0;
                                                            int studentsThatAnswered = 0;
                                                            int totalStudents = 0;

                                                            for (InputTextCardDocument.InputTextCardStudentData studentData : inputTextCardDocument.getStudentsData()) {
                                                                totalStudents++;
                                                                if (studentData.getResponse() != null) {
                                                                    totalGrade = totalGrade + studentData.getMark();
                                                                    studentsThatAnswered++;
                                                                    InputTextCardParent.InputTextCardChild childInputTextCard =
                                                                            new InputTextCardParent.InputTextCardChild(
                                                                                    studentData.getStudentID(),
                                                                                    studentData.getResponse(),
                                                                                    interactivityCardDocumentSnapshot
                                                                            );
                                                                    inputTextCardChildList.add(childInputTextCard);
                                                                }
                                                            }

                                                            if (studentsThatAnswered == 0) {
                                                                newCardParent.setAverageGrade("Ningún estudiante ha contestado la pregunta todavía");
                                                            } else {
                                                                float averageGrade = totalGrade / studentsThatAnswered;
                                                                newCardParent.setAverageGrade("Puntuación media de las respuestas: " + averageGrade + "/10");
                                                            }

                                                            if (studentsThatAnswered == totalStudents) {
                                                                newCardParent.setStudentsThatHaveNotAnswered("Todos los estudiantes han contestado");
                                                            } else {
                                                                newCardParent.setStudentsThatHaveNotAnswered("Faltan por contestar: " + (totalStudents - studentsThatAnswered) + "/" + totalStudents);
                                                            }

                                                            interactivityCardsList.add(newCardParent);

                                                        case InteractivityCardType.TYPE_CHOICES:


                                                        case InteractivityCardType.TYPE_REMINDER:

                                                    }
                                                }

                                                if (!interactivityCardsList.isEmpty()) {
                                                    GroupsInteractivityCardsContainer newContainer = new GroupsInteractivityCardsContainer(groupName, interactivityCardsList);
                                                    cardsList.add(newContainer);
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }
                                        }
                                    });
                        }
                    }
                });

        // Buttons configuration
        ArrayList<FloatingActionButton> buttons = new ArrayList<FloatingActionButton>();
        buttons.add(createInputTextCard);
        buttons.add(createMultichoiceCard);
        buttons.add(createReminderCard);

        ButtonAnimator buttonAnimator = new ButtonAnimator(getContext(), createInteractivityCard, buttons);

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