package com.elcazadordebaterias.coordinapp.fragments.commonfragments;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.CourseParticipantAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.CourseParticipantCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.InteractivityCardType;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.CollectiveGroupDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.InputTextCardDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.interactivitydocuments.MultichoiceCardDocument;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
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


        if (selectedCourse == null || selectedSubject == null) {
            noCourseSelected.setVisibility(View.VISIBLE);
            coursesRecyclerView.setVisibility(View.GONE);
        } else {
            noCourseSelected.setVisibility(View.GONE);
            coursesRecyclerView.setVisibility(View.VISIBLE);
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
                            participants.add(new CourseParticipantCard(R.drawable.ic_teacher_at_the_blackboard, document2.getId(), "Profesor", (String) document2.get("FullName"), (String) document2.get("UserEmail")));
                        }
                        fStore
                                .collection("Students")
                                .whereIn(FieldPath.documentId(), studentIds)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        participants.add(new CourseParticipantCard(R.drawable.ic_reading_book, document.getId(), "Estudiante", (String) document.get("FullName"), (String) document.get("UserEmail")));
                                        allStudentsStatistics.put(document.getId(), new HashMap<String, HashMap<String, Double>>());
                                        courseParticipantAdapter.notifyDataSetChanged();
                                    }
                                    populateStatistics();
                                });
                    });
                });
    }


    private void populateStatistics(){

        for (String key : allStudentsStatistics.keySet()) {
            String studentID = key;
            HashMap<String, HashMap<String, Double>> oneStudentsStatistics = allStudentsStatistics.get(key);

            fStore
                    .collection("CoursesOrganization")
                    .document(selectedCourse)
                    .collection("Subjects")
                    .document(selectedSubject)
                    .collection("CollectiveGroups")
                    .whereArrayContains("allParticipantsIDs", studentID)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            CollectiveGroupDocument collectiveGroupDocument = documentSnapshot.toObject(CollectiveGroupDocument.class);
                            String groupName = collectiveGroupDocument.getName();
                            oneStudentsStatistics.put(groupName, new HashMap<String, Double>());


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

                                        // InputText Statistics
                                        groupStatistics.put("Evaluable InputTextDocuments", evaluableInputTextDocuments);
                                        //Log.d("DEBUGGING", "Evaluable InputTextDocuments "+evaluableInputTextDocuments);
                                        groupStatistics.put("Cumulative InputTextMark", cumulativeInputTextMark);
                                        //Log.d("DEBUGGING", "Cumulative InputTextMark  "+cumulativeInputTextMark);

                                        // Multichoice Statistics
                                        groupStatistics.put("Evaluable MultichoiceDocuments", evaluableMultichoiceDocuments);
                                        //Log.d("DEBUGGING", "Evaluable MultichoiceDocument "+evaluableMultichoiceDocuments);
                                        groupStatistics.put("Total points", totalPoints);
                                        //Log.d("DEBUGGING", "Total points "+cumulativeInputTextMark);

                                    });

                        }

                    });
        }
    }

}
