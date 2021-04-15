package com.elcazadordebaterias.coordinapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.CourseCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.CourseCard;
import com.elcazadordebaterias.coordinapp.utils.CourseParticipant;
import com.elcazadordebaterias.coordinapp.utils.CourseSubject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment_Student_Courses extends Fragment {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_student_courses, container, false);

        RecyclerView coursesRecyclerView = v.findViewById(R.id.recyclerview_courses);
        ArrayList<CourseCard> itemList = new ArrayList<CourseCard>();
        CourseCardAdapter courseCardAdapter = new CourseCardAdapter(itemList);
        LinearLayoutManager coursesLayoutManager = new LinearLayoutManager(getContext());

        coursesRecyclerView.setAdapter(courseCardAdapter);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        // Create the list of the groups a student is in.
        fStore.collection("CoursesOrganization").document("3ºESO B").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) document.get("Subjects"); // Array of the subjects. Contains all the subjects from the current course
                    List<CourseSubject> courseSubjectList = new ArrayList<>();  // List with the information of the subjects

                    for (int i = 0; i < data.size(); i++) { // Iterate over all the subjects in the current course
                        Map<String, Object> subjectInfo = data.get(i); // Current subject information (the list with the students, the name of the subject and the teacher id)

                        ArrayList<String> studentsIds = (ArrayList<String>) subjectInfo.get("Students");
                        List<CourseParticipant> courseParticipantList = new ArrayList<>(); // List with the information of the students

                        if (studentsIds.contains(fAuth.getCurrentUser().getUid())) { // Check if the student is enrolled in the subject

                            fStore.collection("Students").get().addOnCompleteListener(task1 -> { // Search for student info to build the student list
                                if (task1.isSuccessful()) {
                                    // Add the teacher to be displayed
                                    fStore.collection("Teachers").document((String) subjectInfo.get("TeacherId")).get().addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {

                                            DocumentSnapshot document2 = task2.getResult();

                                            courseParticipantList.add(new CourseParticipant("Profesor: " + document2.getData().get("FullName").toString(), document2.getData().get("UserEmail").toString()));

                                            for (QueryDocumentSnapshot document1 : task1.getResult()) { // Create the list of the students
                                                if (studentsIds.contains(document1.getId()) && !document1.getId().equals(fAuth.getCurrentUser().getUid())) { // The current user is not shown in the list
                                                    courseParticipantList.add(new CourseParticipant(document1.getData().get("FullName").toString(), document1.getData().get("UserEmail").toString()));
                                                }
                                            }

                                            courseSubjectList.add(new CourseSubject((String) subjectInfo.get("SubjectName"), courseParticipantList));
                                            courseCardAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            });

                        }
                    }
                    itemList.add(new CourseCard(document.getId(), courseSubjectList));
                }
            }
        });
        return v;
    }

}
