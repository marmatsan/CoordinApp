package com.elcazadordebaterias.coordinapp.fragments.teacherfragments;

import android.os.Bundle;
import android.util.Log;
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
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
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
        View v = inflater.inflate(R.layout.fragment_administration_teacher_courses, container, false);

        RecyclerView ParentRecyclerViewItem = v.findViewById(R.id.recyclerview_courses);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        ParentRecyclerViewItem.setAdapter(courseCardAdapter);
        ParentRecyclerViewItem.setLayoutManager(layoutManager);

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
                            .whereEqualTo("teacherID", fAuth.getUid())
                            .get()
                            .addOnCompleteListener(getTeacherSubjects -> {
                                if (getTeacherSubjects.isSuccessful()) {
                                    for (QueryDocumentSnapshot subjectDocument : getTeacherSubjects.getResult()) { // For each subject that the student is in
                                        Subject subject = subjectDocument.toObject(Subject.class);

                                        ArrayList<String> studentsIDs = subject.getStudentIDs();
                                        List<CourseParticipant> courseParticipantList = new ArrayList<>();

                                        CourseSubject courseSubject = new CourseSubject(subject.getSubjectName(), courseParticipantList);
                                        courseSubjectList.add(courseSubject);

                                        fStore.collection("Students").whereIn(FieldPath.documentId(), studentsIDs).get().addOnCompleteListener(getStudentsInfo -> { // Get the information of the students
                                            if (getStudentsInfo.isSuccessful()) {

                                                for (QueryDocumentSnapshot studentDocument : getStudentsInfo.getResult()) {
                                                    Log.d("DEBUGGING", studentDocument.getId());
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