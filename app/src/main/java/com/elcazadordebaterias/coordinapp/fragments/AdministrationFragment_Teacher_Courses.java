package com.elcazadordebaterias.coordinapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.SubjectCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.StudentCardItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

/**
 * The fragment representing the Courses Tab inside the Administration Tab of the teacher.
 *
 * @author Martín Mateos Sánchez
 */
public class AdministrationFragment_Teacher_Courses extends Fragment {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private RecyclerView subjectsRecyclerview;
    private RecyclerView.Adapter subjectsAdapter;
    private RecyclerView.LayoutManager subjectsLayoutManager;

    ArrayList<StudentCardItem> studentList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        studentList = new ArrayList<StudentCardItem>();
        subjectsAdapter = new SubjectCardAdapter(studentList);

        fStore.collection("CoursesOrganization").document("3ºESO B")
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) document.get("Subjects");
                    Map<String, Object> subjectInfo = data.get(0);
                    ArrayList<String> studentsIds = (ArrayList<String>) subjectInfo.get("Students");

                    fStore.collection("Students")
                            .get()
                            .addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    for (QueryDocumentSnapshot document1 : task1.getResult()) {
                                        if (studentsIds.contains(document1.getId())) {
                                            StudentCardItem student = new StudentCardItem(document1.getData().get("FullName").toString(), document1.getData().get("UserEmail").toString());
                                            Log.d("TEST", student.getStudentName());
                                            studentList.add(student);
                                        }
                                    }
                                    subjectsAdapter.notifyDataSetChanged();
                                }
                            });
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.utils_subjectcard, container, false); //TODO: Change to R.layout.fragment_administration_teacher_courses
        subjectsRecyclerview = v.findViewById(R.id.recyclerView_Groups);
        subjectsLayoutManager = new LinearLayoutManager(getContext());
        subjectsRecyclerview.setLayoutManager(subjectsLayoutManager);
        subjectsRecyclerview.setAdapter(subjectsAdapter);

        return v;
    }
}