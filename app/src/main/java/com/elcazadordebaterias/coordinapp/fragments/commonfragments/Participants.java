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
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.CourseParticipantAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.CourseParticipantCard;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

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

    public Participants(int userType, String selectedCourse, String selectedSubject) {
        this.userType = userType;
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        participants = new ArrayList<CourseParticipantCard>();
        courseParticipantAdapter = new CourseParticipantAdapter(participants, userType);

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
                                        courseParticipantAdapter.notifyDataSetChanged();
                                    }
                                });
                    });
                });
    }

}
