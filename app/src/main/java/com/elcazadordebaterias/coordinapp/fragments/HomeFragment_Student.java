package com.elcazadordebaterias.coordinapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.activities.LoginActivity;
import com.elcazadordebaterias.coordinapp.adapters.AdministrationFragmentTeacherAdapter;
import com.elcazadordebaterias.coordinapp.adapters.CourseCardAdapter;
import com.elcazadordebaterias.coordinapp.adapters.HomeFragmentStudentAdapter;
import com.elcazadordebaterias.coordinapp.adapters.PetitionGroupCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.CourseCard;
import com.elcazadordebaterias.coordinapp.utils.CourseParticipant;
import com.elcazadordebaterias.coordinapp.utils.CourseSubject;
import com.elcazadordebaterias.coordinapp.utils.PetitionGroupCardParticipant;
import com.elcazadordebaterias.coordinapp.utils.PetitionGroupCard;
import com.elcazadordebaterias.coordinapp.utils.PetitionRequest;
import com.elcazadordebaterias.coordinapp.utils.PetitionUser;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The fragment representing the Home Tab of the student.
 *
 * @author Martín Mateos Sánchez
 */
public class HomeFragment_Student extends Fragment {

    HomeFragmentStudentAdapter optionsAdapter;

    private ViewPager2 viewpager;
    private TabLayout tablayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_student, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tablayout = view.findViewById(R.id.tabLayout);
        tablayout.setInlineLabel(false);
        viewpager = view.findViewById(R.id.fragment_container_student_home);

        optionsAdapter = new HomeFragmentStudentAdapter(this);
        viewpager.setAdapter(optionsAdapter);

        new TabLayoutMediator(tablayout, viewpager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Cursos");
                    tab.setIcon(R.drawable.ic_baseline_folder_24);
                    break;
                case 1:
                    tab.setText("Peticiones");
                    tab.setIcon(R.drawable.ic_baseline_notifications_none_24);
                    break;
            }
        }).attach();
    }
}