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
import com.elcazadordebaterias.coordinapp.adapters.ParentParentItemAdapter;
import com.elcazadordebaterias.coordinapp.utils.ChildItem;
import com.elcazadordebaterias.coordinapp.utils.ParentItem;
import com.elcazadordebaterias.coordinapp.utils.ParentParentItem;
import com.google.android.gms.tasks.OnCompleteListener;
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

        List<ParentParentItem> itemList = new ArrayList<>();

        ParentParentItemAdapter parentParentItemAdapter = new ParentParentItemAdapter(itemList);

        ParentRecyclerViewItem.setAdapter(parentParentItemAdapter);
        ParentRecyclerViewItem.setLayoutManager(layoutManager);

        // Create list
        fStore.collection("CoursesOrganization").document("3ºESO B").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) document.get("Subjects"); // Array of the subjects. Contains all the subjects from the current course
                    Map<String, Object> subjectInfo = data.get(0); // Current subject

                    if (subjectInfo.get("TeacherId").equals(fAuth.getCurrentUser().getUid())){ // Check if the teacher is teaching the subject

                        ArrayList<String> studentsIds = (ArrayList<String>) subjectInfo.get("Students");
                        List<ChildItem> ChildItemList = new ArrayList<>(); // List with the information of the students
                        List<ParentItem> ParentItemList = new ArrayList<>();  // List with the information of the subjects

                        fStore.collection("Students").get().addOnCompleteListener(task1 -> { // Search for student info to build the student list
                                    if (task1.isSuccessful()) {

                                        for (QueryDocumentSnapshot document1 : task1.getResult()) { // Create the list of the students
                                            if (studentsIds.contains(document1.getId())) {
                                                ChildItemList.add(new ChildItem(document1.getData().get("FullName").toString(),  document1.getData().get("UserEmail").toString()));
                                            }
                                        }

                                        ParentItemList.add(new ParentItem((String) subjectInfo.get("SubjectName"), ChildItemList));
                                        itemList.add(new ParentParentItem(document.getId(), ParentItemList));

                                        parentParentItemAdapter.notifyDataSetChanged();

                                    }
                                });

                    }
                }
            }
        });

        return v;
    }

}