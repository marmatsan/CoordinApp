package com.elcazadordebaterias.coordinapp.fragments.studentfragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.CourseCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.CourseCard;
import com.elcazadordebaterias.coordinapp.utils.CourseParticipant;
import com.elcazadordebaterias.coordinapp.utils.CourseSubject;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Course;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment_Student_Courses extends Fragment {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ArrayList<CourseCard> coursesList;
    CourseCardAdapter courseCardAdapter;

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
        View v = inflater.inflate(R.layout.fragment_home_student_courses, container, false);

        RecyclerView coursesRecyclerView = v.findViewById(R.id.recyclerview_courses);

        LinearLayoutManager coursesLayoutManager = new LinearLayoutManager(getContext());

        coursesRecyclerView.setAdapter(courseCardAdapter);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        createCoursesList();

        return v;
    }

    private void createCoursesList() {
        fStore.collection("CoursesOrganization").whereArrayContains("allParticipantsIDs", fAuth.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot courseDocument : task.getResult()) { // For each course that the student has at least one subject

                    List<CourseSubject> courseSubjectList = new ArrayList<>();  // List with the information of the subjects

                    CourseCard course = new CourseCard(courseDocument.getId(), courseSubjectList);
                    coursesList.add(course);

                    fStore.collection("CoursesOrganization").document(courseDocument.getId())
                            .collection("Subjects")
                            .whereArrayContains("studentIDs", fAuth.getUid())
                            .get()
                            .addOnCompleteListener(getStudentSubjects -> {
                                if (getStudentSubjects.isSuccessful()) {
                                    for (QueryDocumentSnapshot subjectDocument : getStudentSubjects.getResult()) { // For each subject that the student is in
                                        Subject subject = subjectDocument.toObject(Subject.class);

                                        ArrayList<String> studentsIDs = subject.getStudentIDs();
                                        List<CourseParticipant> courseParticipantList = new ArrayList<>();

                                        CourseSubject courseSubject = new CourseSubject(subject.getSubjectName(), courseParticipantList);
                                        courseSubjectList.add(courseSubject);

                                        fStore.collection("Students").whereIn(FieldPath.documentId(), studentsIDs).get().addOnCompleteListener(getStudentsInfo -> { // Get the information of the students
                                            if (getStudentsInfo.isSuccessful()) {

                                                for (QueryDocumentSnapshot studentDocument : getStudentsInfo.getResult()) {
                                                    CourseParticipant participant = new CourseParticipant("Alumno", (String) studentDocument.get("FullName"), (String) studentDocument.get("UserEmail"));
                                                    courseParticipantList.add(participant);
                                                }
                                                courseCardAdapter.notifyDataSetChanged();

                                                fStore.collection("Teachers").document(subject.getTeacherID()).get().addOnSuccessListener(teacherDocument -> { // Get the information of the teacher
                                                    CourseParticipant participant = new CourseParticipant("Profesor", (String) teacherDocument.get("FullName"), (String) teacherDocument.get("UserEmail"));
                                                    courseParticipantList.add(participant);
                                                    courseCardAdapter.notifyDataSetChanged();
                                                });

                                            }
                                        });
                                    }
                                }
                            });

                }
            }
        });
    }

}
