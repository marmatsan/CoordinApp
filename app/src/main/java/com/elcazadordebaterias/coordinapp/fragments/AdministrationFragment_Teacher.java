package com.elcazadordebaterias.coordinapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.activities.LoginActivity;
import com.elcazadordebaterias.coordinapp.adapters.AdministrationFragmentTeacherAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * The fragment representing the Administration Tab of the teacher.
 *
 * @author Martín Mateos Sánchez
 */
public class AdministrationFragment_Teacher extends Fragment {
    AdministrationFragmentTeacherAdapter optionsAdapter;

    private MaterialButton logout;

    private ViewPager2 viewpager;
    private TabLayout tablayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_administration_teacher, container, false);

        logout = rootView.findViewById(R.id.logout_button);
        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tablayout = view.findViewById(R.id.tabLayout);
        tablayout.setInlineLabel(false);
        viewpager = view.findViewById(R.id.fragment_container_teacher_administration);

        optionsAdapter = new AdministrationFragmentTeacherAdapter(this);
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
                case 2:
                    tab.setText("Fechas");
                    tab.setIcon(R.drawable.ic_baseline_calendar_today_24);
                    break;
            }
        }).attach();
    }

}