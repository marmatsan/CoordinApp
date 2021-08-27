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
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.MultichoiceCardDocument;
import com.elcazadordebaterias.coordinapp.utils.utilities.ButtonAnimator;
import com.google.android.gms.tasks.OnSuccessListener;
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

    HashMap<String, ArrayList<InteractivityCard>> interactivityListsMap;
    HashMap<String, Boolean> hasDocumentsMap;
    HashMap<String, QuerySnapshot> allInteractivityDocumentsSnapshotsMap;
    HashMap<String, HashMap<String, Double>> statisticsMap;

    TextView explicativeError;

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

        explicativeError = view.findViewById(R.id.explicativeError);
        explicativeError.setVisibility(View.GONE);

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

        interactivityListsMap = new HashMap<String, ArrayList<InteractivityCard>>();
        hasDocumentsMap = new HashMap<String, Boolean>();
        allInteractivityDocumentsSnapshotsMap = new HashMap<String, QuerySnapshot>();
        statisticsMap = new HashMap<String, HashMap<String, Double>>();

        fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("CollectiveGroups")
                .get()
                .addOnSuccessListener(collectiveGroupsDocumentSnapshots -> {

                    if (collectiveGroupsDocumentSnapshots.isEmpty()) {
                        explicativeError.setText(R.string.noGroupsInteractivity);
                        explicativeError.setVisibility(View.VISIBLE);
                        hideButtons();
                    } else {
                        showButtons();
                        explicativeError.setVisibility(View.GONE);
                        for (DocumentSnapshot documentSnapshot : collectiveGroupsDocumentSnapshots) {
                            String groupName = documentSnapshot.toObject(CollectiveGroupDocument.class).getName();

                            ArrayList<InteractivityCard> interactivityCardsList = new ArrayList<InteractivityCard>();
                            interactivityListsMap.put(groupName, interactivityCardsList);

                            documentSnapshot
                                    .getReference()
                                    .collection("InteractivityCards")
                                    .addSnapshotListener((interactivityCardsDocumentSnapshots, error1) -> {

                                        if (error1 != null) {
                                            return;
                                        } else if (interactivityCardsDocumentSnapshots == null) {
                                            return;
                                        }

                                        statisticsMap.put(groupName, getCardStatistics(interactivityCardsDocumentSnapshots));

                                        interactivityCardsList.clear();

                                        hasDocumentsMap.put(groupName, !interactivityCardsDocumentSnapshots.isEmpty());
                                        allInteractivityDocumentsSnapshotsMap.put(groupName, interactivityCardsDocumentSnapshots);

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

        createInputTextCard.setOnClickListener(v -> {
            CreateInputTextCardDialog dialog = new CreateInputTextCardDialog(selectedCourse, selectedSubject);
            dialog.show(getParentFragmentManager(), "dialog");
        });

        createMultichoiceCard.setOnClickListener(v -> {
            CreateMultichoiceCardDialog dialog = new CreateMultichoiceCardDialog(selectedCourse, selectedSubject);
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

    private void changeList() {
        interactivityContainerList.clear();
        for (String key : interactivityListsMap.keySet()) {
            ArrayList<InteractivityCard> interactivitiesList = interactivityListsMap.get(key);
            Boolean hasDocuments = hasDocumentsMap.get(key);
            QuerySnapshot allInteractivityDocumentsSnapshots = allInteractivityDocumentsSnapshotsMap.get(key);
            HashMap<String, Double> statistics = statisticsMap.get(key);

            if (interactivitiesList != null && hasDocuments != null && allInteractivityDocumentsSnapshots != null && statistics != null) {
                if (hasDocuments) {
                    interactivityContainerList.add(new InteractivityCardsContainer("Actividades con el " + key, interactivitiesList, allInteractivityDocumentsSnapshots, statistics));
                }
            }
        }

        if (interactivityContainerList.isEmpty()) {
            explicativeError.setText(R.string.noInteractivitiesCreated);
            explicativeError.setVisibility(View.VISIBLE);
        } else {
            explicativeError.setVisibility(View.GONE);
        }

        adapter.notifyDataSetChanged();
    }

    private HashMap<String, Double> getCardStatistics(QuerySnapshot interactivityCardsDocumentSnapshots) {
        HashMap<String, Double> statistics = new HashMap<String, Double>();

        double evaluableGroupalInputCards = 0;
        double groupalMarkInputText = 0;

        double evaluableIndividualStudents = 0;
        double individualMarkInputText = 0;

        double evaluableGroupalMultichoiceCards = 0;
        double totalGroupalAnsweredCorrected = 0;

        double evaluableIndividualMarks = 0;
        double totalIndividualAverage = 0;

        for (DocumentSnapshot interactivityCardsDocumentSnapshot : interactivityCardsDocumentSnapshots) {
            Long cardType = interactivityCardsDocumentSnapshot.getLong("cardType");

            if (cardType != null) {
                if (cardType == InteractivityCardType.TYPE_INPUTTEXT) {
                    InputTextCardDocument inputTextCardDocument = interactivityCardsDocumentSnapshot.toObject(InputTextCardDocument.class);

                    if (inputTextCardDocument.getHasToBeEvaluated()) {
                        if (inputTextCardDocument.getHasGroupalActivity()) {
                            InputTextCardDocument.InputTextCardStudentData groupData = inputTextCardDocument.getStudentsData().get(0);
                            if (groupData.getHasMarkSet()) {
                                evaluableGroupalInputCards++;
                                groupalMarkInputText += groupData.getMark();
                            }
                        } else {
                            for (InputTextCardDocument.InputTextCardStudentData studentData : inputTextCardDocument.getStudentsData()) {
                                if (studentData.getHasMarkSet()) {
                                    evaluableIndividualStudents++;
                                    individualMarkInputText += studentData.getMark();
                                }
                            }
                        }
                    }

                } else if (cardType == InteractivityCardType.TYPE_CHOICES) {
                    MultichoiceCardDocument multichoiceCardDocument = interactivityCardsDocumentSnapshot.toObject(MultichoiceCardDocument.class);

                    if (multichoiceCardDocument.getHasToBeEvaluated()) {

                        int correctQuestionIdentifier = 0;

                        for (MultichoiceCardDocument.Question question : multichoiceCardDocument.getQuestionsList()) {
                            if (question.getHasCorrectAnswer()) {
                                correctQuestionIdentifier = question.getQuestionIdentifier();
                                break;
                            }
                        }

                        if (multichoiceCardDocument.getHasGroupalActivity()) {
                            evaluableGroupalMultichoiceCards++;
                            MultichoiceCardDocument.MultichoiceCardStudentData groupData = multichoiceCardDocument.getStudentsData().get(0);

                            if (groupData.getQuestionRespondedIdentifier() == correctQuestionIdentifier) {
                                totalGroupalAnsweredCorrected += totalGroupalAnsweredCorrected;
                            }
                        } else {

                            evaluableIndividualMarks++;
                            int totalStudents = multichoiceCardDocument.getStudentsData().size();
                            int answeredCorrectly = 0;

                            for (MultichoiceCardDocument.MultichoiceCardStudentData studentData : multichoiceCardDocument.getStudentsData()) {
                                if (studentData.getQuestionRespondedIdentifier() == correctQuestionIdentifier) {
                                    answeredCorrectly += 1;
                                }
                            }

                            totalIndividualAverage += (answeredCorrectly / totalStudents) * 100;

                        }
                    }

                }
            }
        }

        // InputText Statistics
        // Groupal InputText mark
        statistics.put("Groupal InputText Mark", groupalMarkInputText);
        statistics.put("Groupal InputText Cards", evaluableGroupalInputCards);

        // Individual InputText mark
        statistics.put("Individual InputText Mark", individualMarkInputText);
        statistics.put("Individual Evaluable Students", evaluableIndividualStudents);

        // Multichoice Statistics
        // Groupal Multichoice mark
        statistics.put("Groupal Multichoice Mark", totalGroupalAnsweredCorrected);
        statistics.put("Groupal Multichoice Cards", evaluableGroupalMultichoiceCards);

        // Individual Multichoice mark
        statistics.put("Individual Multichoice Mark", evaluableIndividualMarks);
        statistics.put(" ", totalIndividualAverage);

        return statistics;
    }

}