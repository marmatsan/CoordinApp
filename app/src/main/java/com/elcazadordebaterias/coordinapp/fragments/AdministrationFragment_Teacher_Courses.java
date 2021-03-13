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

/**
 * The fragment representing the Courses Tab inside the Administration Tab of the teacher.
 *
 * @author Martín Mateos Sánchez
 */
public class AdministrationFragment_Teacher_Courses extends Fragment {

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
        View v = inflater.inflate(R.layout.fragment_administration_teacher_courses, container, false);

        RecyclerView ParentRecyclerViewItem = v.findViewById(R.id.recyclerview_courses);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        List<CourseCard> itemList = new ArrayList<>();

        CourseCardAdapter courseCardAdapter = new CourseCardAdapter(itemList);

        ParentRecyclerViewItem.setAdapter(courseCardAdapter);
        ParentRecyclerViewItem.setLayoutManager(layoutManager);

        // Create list
        fStore.collection("CoursesOrganization").document("3ºESO B").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) document.get("Subjects"); // Array of the subjects. Contains all the subjects from the current course
                    List<CourseSubject> courseSubjectList = new ArrayList<>();  // List with the information of the subjects

                    for (int i = 0; i < data.size(); i++) { // Iterate over all the subjects in the current course
                        Map<String, Object> subjectInfo = data.get(i); // Current subject

                        ArrayList<String> studentsIds = (ArrayList<String>) subjectInfo.get("Students");
                        List<CourseParticipant> courseParticipantList = new ArrayList<>(); // List with the information of the students

                        if (subjectInfo.get("TeacherId").equals(fAuth.getCurrentUser().getUid())) { // Check if the teacher is teaching the subject

                            fStore.collection("Students").get().addOnCompleteListener(task1 -> { // Search for student info to build the student list
                                if (task1.isSuccessful()) {

                                    for (QueryDocumentSnapshot document1 : task1.getResult()) { // Create the list of the students
                                        if (studentsIds.contains(document1.getId())) {
                                            courseParticipantList.add(new CourseParticipant(document1.getData().get("FullName").toString(), document1.getData().get("UserEmail").toString()));
                                        }
                                    }

                                    courseSubjectList.add(new CourseSubject((String) subjectInfo.get("SubjectName"), courseParticipantList));
                                    courseCardAdapter.notifyDataSetChanged();
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