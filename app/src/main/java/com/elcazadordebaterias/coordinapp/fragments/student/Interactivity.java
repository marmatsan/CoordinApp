package com.elcazadordebaterias.coordinapp.fragments.student;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.interactivity.student.GroupsInteractivityCardsAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards.GroupsInteractivityCardsContainer;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards.InputTextCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards.InteractivityCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards.MultichoiceCard;
import com.elcazadordebaterias.coordinapp.utils.cards.interactivity.studentcards.StandByCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.InteractivityCardType;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.CollectiveGroupDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.InputTextCardDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.MultichoiceCardDocument;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private GroupsInteractivityCardsAdapter adapter;
    private ArrayList<GroupsInteractivityCardsContainer> cardsList;

    private TextView emptyInteractivities;

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

        emptyInteractivities = view.findViewById(R.id.emptyInteractivities);

        // Container for the interactivity cards
        RecyclerView interactivitiesContainer = view.findViewById(R.id.interactivitiesContainer);

        cardsList = new ArrayList<GroupsInteractivityCardsContainer>();
        adapter = new GroupsInteractivityCardsAdapter(cardsList, getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        interactivitiesContainer.setAdapter(adapter);
        interactivitiesContainer.setLayoutManager(layoutManager);

        fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("CollectiveGroups")
                .whereArrayContains("allParticipantsIDs", fAuth.getUid())
                .addSnapshotListener((collectiveGroupsDocumentSnapshots, error) -> {

                    if (error != null) {
                        return;
                    } else if (collectiveGroupsDocumentSnapshots == null) {
                        return;
                    }

                    if (collectiveGroupsDocumentSnapshots.isEmpty()) {
                        String noGroups = "Todavía no estás en ningún grupo. Cuando se cree un nuevo grupo el profesor podrá enviar actividades";
                        emptyInteractivities.setText(noGroups);
                    } else {
                        String noActivities = "Todavía no estás en ningún grupo. Cuando se cree un nuevo grupo el profesor podrá enviar actividades";
                        emptyInteractivities.setText(noActivities);
                    }

                    cardsList.clear();
                    for (DocumentSnapshot collectiveGroupDocumentSnapshot : collectiveGroupsDocumentSnapshots) {
                        CollectiveGroupDocument collectiveGroupDocument = collectiveGroupDocumentSnapshot.toObject(CollectiveGroupDocument.class);

                        String groupName = collectiveGroupDocument.getName();
                        String spokerID = collectiveGroupDocument.getSpokesStudentID();
                        String spokerName = collectiveGroupDocument.getSpokerName();

                        collectiveGroupDocumentSnapshot
                                .getReference()
                                .collection("InteractivityCards")
                                .addSnapshotListener((interactivityCardsDocumentSnapshots, error1) -> {

                                    if (error1 != null) {
                                        return;
                                    } else if (interactivityCardsDocumentSnapshots == null) {
                                        return;
                                    }

                                    cardsList.clear();
                                    ArrayList<InteractivityCard> interactivityCardsList = new ArrayList<InteractivityCard>();
                                    GroupsInteractivityCardsContainer interactivityCardsContainer = new GroupsInteractivityCardsContainer("Actividades con el " + groupName, interactivityCardsList);

                                    for (DocumentSnapshot interactivityCardDocumentSnapshot : interactivityCardsDocumentSnapshots) {

                                        Long cardType = interactivityCardDocumentSnapshot.getLong("cardType");

                                        if (cardType != null) {
                                            switch (cardType.intValue()) {
                                                case InteractivityCardType.TYPE_INPUTTEXT:
                                                    InputTextCardDocument inputTextCardDocument = interactivityCardDocumentSnapshot.toObject(InputTextCardDocument.class);

                                                    if (inputTextCardDocument.getHasGroupalActivity()) {
                                                        InputTextCardDocument.InputTextCardStudentData studentData = inputTextCardDocument.getStudentsData().get(0);

                                                        if (studentData.getResponse() == null) {
                                                            if (fAuth.getUid().equals(spokerID)) {
                                                                InputTextCard inputTextCard = new InputTextCard(inputTextCardDocument.getTitle(), studentData.getStudentID(), interactivityCardDocumentSnapshot);
                                                                interactivityCardsList.add(inputTextCard);
                                                            } else {
                                                                StandByCard standByCard = new StandByCard(inputTextCardDocument.getTitle(), null, spokerName, InteractivityCardType.TYPE_INPUTTEXT);
                                                                interactivityCardsList.add(standByCard);
                                                            }
                                                        }
                                                    } else {
                                                        for (InputTextCardDocument.InputTextCardStudentData studentData : inputTextCardDocument.getStudentsData()) {
                                                            if (studentData.getStudentID().equals(fAuth.getUid()) && studentData.getResponse() == null) {
                                                                InputTextCard inputTextCard = new InputTextCard(inputTextCardDocument.getTitle(), studentData.getStudentID(), interactivityCardDocumentSnapshot);
                                                                interactivityCardsList.add(inputTextCard);
                                                                break;
                                                            }
                                                        }
                                                    }

                                                    break;
                                                case InteractivityCardType.TYPE_CHOICES:
                                                    MultichoiceCardDocument multichoiceCardDocument = interactivityCardDocumentSnapshot.toObject(MultichoiceCardDocument.class);

                                                    if (multichoiceCardDocument.getHasGroupalActivity()) {
                                                        MultichoiceCardDocument.MultichoiceCardStudentData studentData = multichoiceCardDocument.getStudentsData().get(0);

                                                        if (studentData.getQuestionRespondedIdentifier() == -1) {
                                                            if (fAuth.getUid().equals(spokerID)) {
                                                                MultichoiceCard multiChoiceCard = new MultichoiceCard(multichoiceCardDocument.getTitle(), studentData.getStudentID(), multichoiceCardDocument.getQuestionsList(), interactivityCardDocumentSnapshot);
                                                                interactivityCardsList.add(multiChoiceCard);
                                                            } else {
                                                                StandByCard standByCard = new StandByCard(multichoiceCardDocument.getTitle(), null, spokerName, InteractivityCardType.TYPE_CHOICES);
                                                                interactivityCardsList.add(standByCard);
                                                            }
                                                        }
                                                    } else {
                                                        for (MultichoiceCardDocument.MultichoiceCardStudentData studentData : multichoiceCardDocument.getStudentsData()) {
                                                            if (studentData.getStudentID().equals(fAuth.getUid()) && studentData.getQuestionRespondedIdentifier() == -1) { //
                                                                MultichoiceCard multiChoiceCard = new MultichoiceCard(multichoiceCardDocument.getTitle(), studentData.getStudentID(), multichoiceCardDocument.getQuestionsList(), interactivityCardDocumentSnapshot);
                                                                interactivityCardsList.add(multiChoiceCard);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    break;
                                                default: // Reminder

                                            }
                                        }

                                    }

                                    if (!interactivityCardsList.isEmpty()) {
                                        cardsList.add(interactivityCardsContainer);
                                    }

                                    listChanged();
                                });

                    }
                });

        return view;
    }

    private void listChanged() {
        adapter.notifyDataSetChanged();
        if (cardsList.isEmpty()) {
            emptyInteractivities.setVisibility(View.VISIBLE);
        } else {
            emptyInteractivities.setVisibility(View.GONE);
        }

    }

}