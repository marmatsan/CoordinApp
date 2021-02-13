package com.elcazadordebaterias.coordinapp.fragments;

import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

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

        /*

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
         */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_administration_teacher_courses, container, false);

        RecyclerView ParentRecyclerViewItem = v.findViewById(R.id.recyclerview_courses);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        ParentParentItemAdapter parentParentItemAdapter = new ParentParentItemAdapter(ParentParentItemList());

        ParentRecyclerViewItem.setAdapter(parentParentItemAdapter);
        ParentRecyclerViewItem.setLayoutManager(layoutManager);

        return v;
    }

    private List<ParentItem> ParentItemList1() {
        List<ParentItem> itemList = new ArrayList<>();

        itemList.add(new ParentItem("Asignatura 1", ChildItemList()));
        itemList.add(new ParentItem("Asignatura 2", ChildItemList()));
        itemList.add(new ParentItem("Asignatura 3", ChildItemList()));
        itemList.add(new ParentItem("Asignatura 4", ChildItemList()));

        return itemList;
    }

    private List<ParentItem> ParentItemList2() {
        List<ParentItem> itemList = new ArrayList<>();

        itemList.add(new ParentItem("Asignatura 5", ChildItemList()));
        itemList.add(new ParentItem("Asignatura 6", ChildItemList()));
        itemList.add(new ParentItem("Asignatura 7", ChildItemList()));
        itemList.add(new ParentItem("Asignatura 8", ChildItemList()));

        return itemList;
    }

    private List<ParentItem> ParentItemList3() {
        List<ParentItem> itemList = new ArrayList<>();

        itemList.add(new ParentItem("Asignatura 9", ChildItemList()));
        itemList.add(new ParentItem("Asignatura 10", ChildItemList()));
        itemList.add(new ParentItem("Asignatura 11", ChildItemList()));
        itemList.add(new ParentItem("Asignatura 12", ChildItemList()));

        return itemList;
    }

    private List<ParentItem> ParentItemList4() {
        List<ParentItem> itemList = new ArrayList<>();

        itemList.add(new ParentItem("Asignatura 13", ChildItemList()));
        itemList.add(new ParentItem("Asignatura 14", ChildItemList()));
        itemList.add(new ParentItem("Asignatura 15", ChildItemList()));
        itemList.add(new ParentItem("Asignatura 16", ChildItemList()));

        return itemList;
    }

    private List<ChildItem> ChildItemList() {
        List<ChildItem> ChildItemList = new ArrayList<>();

        ChildItemList.add(new ChildItem("Estudiante 1", "Email1@gmail.com"));
        ChildItemList.add(new ChildItem("Estudiante 2", "Email2@gmail.com"));
        ChildItemList.add(new ChildItem("Estudiante 3", "Email3@gmail.com"));
        ChildItemList.add(new ChildItem("Estudiante 4", "Email4@gmail.com"));

        return ChildItemList;
    }

    private List<ParentParentItem> ParentParentItemList(){
        List<ParentParentItem> itemList = new ArrayList<>();
        itemList.add(new ParentParentItem("Grupo 1",ParentItemList1()));
        itemList.add(new ParentParentItem("Grupo 2", ParentItemList2()));
        itemList.add(new ParentParentItem("Grupo 3", ParentItemList3()));
        itemList.add(new ParentParentItem("Grupo 4", ParentItemList4()));
        return itemList;

    }

}