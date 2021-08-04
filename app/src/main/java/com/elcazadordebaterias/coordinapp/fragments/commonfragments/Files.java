package com.elcazadordebaterias.coordinapp.fragments.commonfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.CourseParticipantAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.CourseParticipantCard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Files extends Fragment {

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

    public Files(int userType, String selectedCourse, String selectedSubject) {
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
        courseParticipantAdapter = new CourseParticipantAdapter(participants);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commonfragments_files, container, false);

        TextView noFiles = view.findViewById(R.id.noFiles);
        RecyclerView filesContainer = view.findViewById(R.id.filesContainer);

        /*
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
        */

        return view;
    }

}
