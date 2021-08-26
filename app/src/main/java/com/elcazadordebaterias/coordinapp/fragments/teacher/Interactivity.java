package com.elcazadordebaterias.coordinapp.fragments.teacher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.interactivity.teacher.GroupsInteractivityCardsContainerAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.InteractivityCardsContainer;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.InputTextCardParent;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.InteractivityCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.teachercards.MultichoiceCard;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Interactivity extends Fragment {

    // Firestore
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private String selectedCourse;
    private String selectedSubject;

    // Buttons
    FloatingActionButton createInteractivityCard;
    FloatingActionButton createInputTextCard;
    FloatingActionButton createMultichoiceCard;
    FloatingActionButton createReminderCard;

    ArrayList<InteractivityCardsContainer> interactivityContainerList;
    GroupsInteractivityCardsContainerAdapter adapter;

    HashMap<String, ArrayList<InteractivityCard>> info;

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

        // InteractivityCardsContainer
        RecyclerView interactivityCardsContainerRecyclerView = view.findViewById(R.id.interactivityCardsContainer);
        interactivityContainerList = new ArrayList<InteractivityCardsContainer>();
        adapter = new GroupsInteractivityCardsContainerAdapter(interactivityContainerList, getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        interactivityCardsContainerRecyclerView.setAdapter(adapter);
        interactivityCardsContainerRecyclerView.setLayoutManager(layoutManager);

        info = new HashMap<String, ArrayList<InteractivityCard>>();

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

                    info.clear();
                    if (collectiveGroupsDocumentSnapshots.isEmpty()) {
                        hideButtons();
                    } else {
                        showButtons();
                        for (DocumentSnapshot documentSnapshot : collectiveGroupsDocumentSnapshots) {
                            String groupName = documentSnapshot.toObject(CollectiveGroupDocument.class).getName();

                            ArrayList<InteractivityCard> interactivityCardsList = new ArrayList<InteractivityCard>();
                            info.put(groupName, interactivityCardsList);

                            documentSnapshot
                                    .getReference()
                                    .collection("InteractivityCards")
                                    .addSnapshotListener((interactivityCardsDocumentSnapshots, error1) -> {

                                        if (error1 != null) {
                                            return;
                                        } else if (interactivityCardsDocumentSnapshots == null) {
                                            return;
                                        }

                                        ArrayList<Integer> statistics = getCardStatistics(interactivityCardsDocumentSnapshots);

                                        adapter.setQueryDocumentSnapshots(interactivityCardsDocumentSnapshots);

                                        interactivityCardsList.clear();
                                        for (DocumentSnapshot interactivityCardDocumentSnapshot : interactivityCardsDocumentSnapshots) {

                                            Long cardType = interactivityCardDocumentSnapshot.getLong("cardType");

                                            if (cardType != null) {
                                                switch (cardType.intValue()) {
                                                    case InteractivityCardType.TYPE_INPUTTEXT:
                                                        InputTextCardParent inputTextCardParent = new InputTextCardParent(interactivityCardDocumentSnapshot);

                                                        if (inputTextCardParent.getHasTeacherVisibility()) {
                                                            interactivityCardsList.add(inputTextCardParent);
                                                        }

                                                        break;
                                                    case InteractivityCardType.TYPE_CHOICES:

                                                        MultichoiceCard multichoiceCard = new MultichoiceCard(interactivityCardDocumentSnapshot);

                                                        if (multichoiceCard.getHasTeacherVisibility()) {
                                                            interactivityCardsList.add(multichoiceCard);
                                                        }

                                                        break;
                                                    case InteractivityCardType.TYPE_REMINDER:
                                                        break;
                                                }
                                            }
                                        }
                                        changeList();
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

        createInputTextCard.setOnClickListener(v ->

        {
            CreateInputTextCardDialog dialog = new CreateInputTextCardDialog(selectedCourse, selectedSubject);
            dialog.show(getParentFragmentManager(), "dialog");
        });

        createMultichoiceCard.setOnClickListener(v ->

        {
            CreateMultichoiceCardDialog dialog = new CreateMultichoiceCardDialog(selectedCourse, selectedSubject);
            dialog.show(getParentFragmentManager(), "dialog");
        });

        createReminderCard.setOnClickListener(v ->

        {
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

    private void changeList() {
        interactivityContainerList.clear();
        for (String key : info.keySet()) {
            interactivityContainerList.add(new InteractivityCardsContainer("Actividades con el " + key, info.get(key)));
        }
        adapter.notifyDataSetChanged();
    }

    private ArrayList<Integer> getCardStatistics(QuerySnapshot interactivityCardsDocumentSnapshots) {
        ArrayList<Integer> statistics = new ArrayList<Integer>();

        int evaluableGroupalTextCards = 0;
        int totalGroupalTextMark = 0;

        for (DocumentSnapshot interactivityCardsDocumentSnapshot : interactivityCardsDocumentSnapshots) {
            Long cardType = interactivityCardsDocumentSnapshot.getLong("cardType");

            if (cardType != null) {
                if (cardType == InteractivityCardType.TYPE_INPUTTEXT) {
                    InputTextCardDocument inputTextCardDocument = interactivityCardsDocumentSnapshot.toObject(InputTextCardDocument.class);

                    if (inputTextCardDocument.getHasToBeEvaluated()) {
                        if (inputTextCardDocument.getHasGroupalActivity()) {
                            InputTextCardDocument.InputTextCardStudentData groupData = inputTextCardDocument.getStudentsData().get(0);
                            if (groupData.getHasMarkSet()) {
                                evaluableGroupalTextCards++;
                                totalGroupalTextMark += groupData.getMark();
                            }
                        }


                    } else {

                    }

                } else if (cardType == InteractivityCardType.TYPE_CHOICES) {

                }
            }
        }

        statistics.add(evaluableGroupalTextCards);
        statistics.add(totalGroupalTextMark);

        return statistics;
    }

}