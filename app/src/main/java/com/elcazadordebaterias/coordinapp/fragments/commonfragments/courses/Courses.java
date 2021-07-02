package com.elcazadordebaterias.coordinapp.fragments.commonfragments.courses;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.courses.CourseCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.courses.CourseCard;
import com.elcazadordebaterias.coordinapp.utils.cards.courses.CourseParticipantCard;
import com.elcazadordebaterias.coordinapp.utils.cards.courses.CourseSubjectCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Courses extends Fragment {

    // Firestore
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    // List of courses
    ArrayList<CourseCard> coursesList;
    CourseCardAdapter courseCardAdapter;

    // Type of user who called this fragment
    private final int userType;

    public Courses(int userType) {
        this.userType = userType;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        coursesList = new ArrayList<CourseCard>();
        courseCardAdapter = new CourseCardAdapter(coursesList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_courses, container, false);

        RecyclerView coursesRecyclerView = view.findViewById(R.id.coursesContainer);

        LinearLayoutManager coursesLayoutManager = new LinearLayoutManager(getContext());

        coursesRecyclerView.setAdapter(courseCardAdapter);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        createCoursesList();

        return view;
    }

    private void createCoursesList() {
        fStore.collection("CoursesOrganization").whereArrayContains("allParticipantsIDs", fAuth.getUid()).get().addOnSuccessListener(queryDocumentSnapshot -> {
            for (QueryDocumentSnapshot courseDocument : queryDocumentSnapshot) { // For each course that the student has at least one subject

                ArrayList<CourseSubjectCard> courseSubjectList = new ArrayList<>();  // List with the information of the subjects

                CourseCard course = new CourseCard(courseDocument.getId(), courseSubjectList);
                coursesList.add(course);

                CollectionReference subjectsCollection = fStore
                        .collection("CoursesOrganization").document(courseDocument.getId())
                        .collection("Subjects");

                if (userType == UserType.TYPE_STUDENT) {
                    subjectsCollection
                            .whereArrayContains("studentIDs", fAuth.getUid())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                buildParticipantsList(queryDocumentSnapshots, courseSubjectList);
                            });
                } else if (userType == UserType.TYPE_TEACHER) {
                    subjectsCollection
                            .whereEqualTo("teacherID", fAuth.getUid())
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                buildParticipantsList(queryDocumentSnapshots, courseSubjectList);
                            });
                }
            }
        });
    }

    private void buildParticipantsList(QuerySnapshot queryDocumentSnapshots, ArrayList<CourseSubjectCard> courseSubjectList){
        for (QueryDocumentSnapshot subjectDocument : queryDocumentSnapshots) { // For each subject that the student is in
            Subject subject = subjectDocument.toObject(Subject.class);

            ArrayList<String> studentsIDs = subject.getStudentIDs();
            ArrayList<CourseParticipantCard> courseParticipantList = new ArrayList<>();

            CourseSubjectCard courseSubject = new CourseSubjectCard(subject.getSubjectName(), courseParticipantList);
            courseSubjectList.add(courseSubject);

            fStore.collection("Students").whereIn(FieldPath.documentId(), studentsIDs).get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                for (QueryDocumentSnapshot studentDocument : queryDocumentSnapshots1) {
                    CourseParticipantCard participant = new CourseParticipantCard("Alumno", (String) studentDocument.get("FullName"), (String) studentDocument.get("UserEmail"));
                    courseParticipantList.add(participant);
                }
                courseCardAdapter.notifyDataSetChanged();

                fStore.collection("Teachers").document(subject.getTeacherID()).get().addOnSuccessListener(teacherDocument -> { // Get the information of the teacher
                    CourseParticipantCard participant = new CourseParticipantCard("Profesor", (String) teacherDocument.get("FullName"), (String) teacherDocument.get("UserEmail"));
                    courseParticipantList.add(participant);
                    courseCardAdapter.notifyDataSetChanged();
                });
            }); // Get the information of the students and the teacher
        }
    }

}
