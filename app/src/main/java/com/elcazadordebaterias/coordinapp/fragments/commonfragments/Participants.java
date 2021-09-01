package com.elcazadordebaterias.coordinapp.fragments.commonfragments;

import android.content.Context;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.CourseParticipantAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.ChatMessageCard;
import com.elcazadordebaterias.coordinapp.utils.cards.CourseParticipantCard;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.InteractivityCardType;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.CollectiveGroupDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.InputTextCardDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.MultichoiceCardDocument;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Participants extends Fragment {

    // Firestore
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    // List of courses
    ArrayList<CourseParticipantCard> participants;
    CourseParticipantAdapter courseParticipantAdapter;

    // Type of user who called this fragment
    private final int userType;

    private String selectedCourse;
    private String selectedSubject;

    private HashMap<String, HashMap<String, HashMap<String, Double>>> allStudentsStatistics;

    private TextInputLayout searchLayout;
    private EditText searchText;

    public Participants(int userType, String selectedCourse, String selectedSubject) {
        this.userType = userType;
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
        this.allStudentsStatistics = new HashMap<String, HashMap<String, HashMap<String, Double>>>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        participants = new ArrayList<CourseParticipantCard>();
        courseParticipantAdapter = new CourseParticipantAdapter(selectedCourse, selectedSubject, participants, userType, getContext(), allStudentsStatistics);

        // Populate participants list
        if (selectedCourse != null && selectedSubject != null) {
            populateParticipantsList();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_participants, container, false);

        TextView noCourseSelected = view.findViewById(R.id.noCourseSelected);
        RecyclerView coursesRecyclerView = view.findViewById(R.id.coursesContainer);

        searchLayout = view.findViewById(R.id.searchLayout);
        searchText = view.findViewById(R.id.searchText);
        searchLayout.setVisibility(View.GONE);
        searchText.setVisibility(View.GONE);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        if (selectedCourse == null || selectedSubject == null) {
            noCourseSelected.setVisibility(View.VISIBLE);
            coursesRecyclerView.setVisibility(View.GONE);
            searchLayout.setVisibility(View.GONE);
            searchText.setVisibility(View.GONE);

        } else {
            noCourseSelected.setVisibility(View.GONE);
            coursesRecyclerView.setVisibility(View.VISIBLE);
            if (!(userType == UserType.TYPE_STUDENT)) {
                searchLayout.setVisibility(View.VISIBLE);
                searchText.setVisibility(View.VISIBLE);
            }
        }

        LinearLayoutManager coursesLayoutManager = new LinearLayoutManager(getContext());

        Context context = getContext();
        if (context != null) {
            DividerItemDecoration divider = new DividerItemDecoration(context, coursesLayoutManager.getOrientation());
            coursesRecyclerView.addItemDecoration(divider);

        }
        coursesRecyclerView.setAdapter(courseParticipantAdapter);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        return view;
    }

    private void populateParticipantsList() {
        fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Subject subject = documentSnapshot.toObject(Subject.class);
                    ArrayList<String> studentIds = subject.getStudentIDs();
                    String teacherId = subject.getTeacherID();

                    fStore
                            .collection("Teachers")
                            .document(teacherId)
                            .get().addOnSuccessListener(document2 -> {
                        if (!teacherId.equals(fAuth.getUid())) {
                            String teacherName = (String) document2.get("FullName");
                            int image;
                            if (teacherName.equals("Docente1") || teacherName.equals("Docente3")) {
                                image = R.drawable.teacher1;
                            } else {
                                image = R.drawable.teacher2;
                            }
                            participants.add(new CourseParticipantCard(image, document2.getId(), "Docente", teacherName, (String) document2.get("UserEmail")));
                        }
                        fStore
                                .collection("Students")
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        if (studentIds.contains(document.getId())) {
                                            String studentName = (String) document.get("FullName");
                                            String numberOnly = studentName.replaceAll("[^0-9]", "");
                                            int studentID = Integer.parseInt(numberOnly);
                                            int image;

                                            if (studentID % 2 == 0) {
                                                image = R.drawable.student1;
                                            } else {
                                                image = R.drawable.student2;
                                            }

                                            participants.add(new CourseParticipantCard(image, document.getId(), "Estudiante", (String) document.get("FullName"), (String) document.get("UserEmail")));
                                            allStudentsStatistics.put(document.getId(), new HashMap<String, HashMap<String, Double>>());
                                        }
                                    }

                                    Collections.sort(participants, new Comparator<CourseParticipantCard>() {
                                        @Override
                                        public int compare(CourseParticipantCard courseParticipantCard1, CourseParticipantCard courseParticipantCard2) {
                                            String participantName1 = courseParticipantCard1.getParticipantName();
                                            String participantName2 = courseParticipantCard2.getParticipantName();


                                            return extractInt(participantName1) - extractInt(participantName2);
                                        }

                                        @Override
                                        public boolean equals(Object o) {
                                            return false;
                                        }

                                        int extractInt(String s) {
                                            String num = s.replaceAll("\\D", "");
                                            return num.isEmpty() ? 0 : Integer.parseInt(num);
                                        }

                                    });

                                    courseParticipantAdapter.notifyDataSetChanged();
                                    populateStatistics();
                                });
                    });
                });
    }


    private void populateStatistics() {

        for (String key : allStudentsStatistics.keySet()) {
            String studentID = key;
            HashMap<String, HashMap<String, Double>> oneStudentsStatistics = allStudentsStatistics.get(key);

            CollectionReference collectiveGroupsRef = fStore
                    .collection("CoursesOrganization")
                    .document(selectedCourse)
                    .collection("Subjects")
                    .document(selectedSubject)
                    .collection("CollectiveGroups");

            collectiveGroupsRef
                    .whereArrayContains("allParticipantsIDs", studentID)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {


                            CollectiveGroupDocument collectiveGroupDocument = documentSnapshot.toObject(CollectiveGroupDocument.class);
                            String groupName = collectiveGroupDocument.getName();
                            oneStudentsStatistics.put(groupName, new HashMap<String, Double>());


                            documentSnapshot
                                    .getReference()
                                    .collection("ChatRoomWithoutTeacher")
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots12 -> {

                                        double totalMessages = queryDocumentSnapshots12.size();
                                        double sentByUser = 0;

                                        for (DocumentSnapshot documentSnapshot1 : queryDocumentSnapshots12) {
                                            ChatMessageCard messageCard = documentSnapshot1.toObject(ChatMessageCard.class);
                                            if (messageCard.getSenderId().equals(studentID)) {
                                                sentByUser++;
                                            }
                                        }

                                        double finalSentByUser = sentByUser;
                                        documentSnapshot
                                                .getReference()
                                                .collection("InteractivityCards")
                                                .get()
                                                .addOnSuccessListener(queryDocumentSnapshots1 -> {

                                                    double evaluableInputTextDocuments = 0;
                                                    double cumulativeInputTextMark = 0;

                                                    double totalPoints = 0;
                                                    double evaluableMultichoiceDocuments = 0;

                                                    for (DocumentSnapshot documentSnapshot1 : queryDocumentSnapshots1) {

                                                        Long cardType = documentSnapshot1.getLong("cardType");

                                                        if (cardType != null) {
                                                            switch (cardType.intValue()) {
                                                                case InteractivityCardType.TYPE_INPUTTEXT:
                                                                    InputTextCardDocument inputTextCardDocument = documentSnapshot1.toObject(InputTextCardDocument.class);

                                                                    for (InputTextCardDocument.InputTextCardStudentData studentData : inputTextCardDocument.getStudentsData()) {
                                                                        if (studentData.getStudentID().equals(studentID)) {
                                                                            if (studentData.getHasMarkSet()) {
                                                                                evaluableInputTextDocuments++;
                                                                                cumulativeInputTextMark += studentData.getMark();
                                                                            }
                                                                        }
                                                                    }


                                                                    break;
                                                                case InteractivityCardType.TYPE_CHOICES:
                                                                    MultichoiceCardDocument multichoiceCardDocument = documentSnapshot1.toObject(MultichoiceCardDocument.class);

                                                                    for (MultichoiceCardDocument.MultichoiceCardStudentData studentData : multichoiceCardDocument.getStudentsData()) {
                                                                        if (studentData.getStudentID().equals(studentID)) {
                                                                            if (studentData.getQuestionRespondedIdentifier() != -1) {
                                                                                evaluableMultichoiceDocuments++;
                                                                                totalPoints += studentData.getMark();
                                                                            }
                                                                        }
                                                                    }

                                                                    break;
                                                            }
                                                        }
                                                    }

                                                    HashMap<String, Double> groupStatistics = oneStudentsStatistics.get(groupName);

                                                    // Chat messages
                                                    groupStatistics.put("Total Chat Messages", totalMessages);
                                                    groupStatistics.put("Messages By User", finalSentByUser);

                                                    // InputText Statistics
                                                    groupStatistics.put("Evaluable InputTextDocuments", evaluableInputTextDocuments);
                                                    groupStatistics.put("Cumulative InputTextMark", cumulativeInputTextMark);

                                                    // Multichoice Statistics
                                                    groupStatistics.put("Evaluable MultichoiceDocuments", evaluableMultichoiceDocuments);
                                                    groupStatistics.put("Total points", totalPoints);
                                                });


                                    });

                        }

                    });
        }
    }

    private void filter(String inputText) {
        if (inputText.isEmpty()) {
            courseParticipantAdapter.filteredList(participants);
        } else {
            ArrayList<CourseParticipantCard> filteredList = new ArrayList<CourseParticipantCard>();
            for (CourseParticipantCard card : participants) {
                if (card.getParticipantName().contains(inputText)) {
                    filteredList.add(card);
                }
            }
            courseParticipantAdapter.filteredList(filteredList);
        }
    }

}
