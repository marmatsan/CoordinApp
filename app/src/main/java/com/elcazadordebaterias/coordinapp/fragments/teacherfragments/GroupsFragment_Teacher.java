package com.elcazadordebaterias.coordinapp.fragments.teacherfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.tablayout.teacher.AdministrationFragmentTeacherAdapter;
import com.elcazadordebaterias.coordinapp.adapters.tablayout.teacher.GroupsFragmentTeacherAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * The fragment representing the Groups/Chat Tab of the teacher.
 * @author Martín Mateos Sánchez
 */
public class GroupsFragment_Teacher extends Fragment {
    GroupsFragmentTeacherAdapter optionsAdapter;

    private ViewPager2 viewpager;
    private TabLayout tablayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groups_teacher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tablayout = view.findViewById(R.id.tabLayout);
        tablayout.setInlineLabel(false);
        viewpager = view.findViewById(R.id.fragment_groups_container);

        optionsAdapter = new GroupsFragmentTeacherAdapter(this);
        viewpager.setAdapter(optionsAdapter);

        new TabLayoutMediator(tablayout, viewpager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Profesor/a");
                    tab.setIcon(R.drawable.ic_group);
                    break;
                case 1:
                    tab.setText("Individuales");
                    tab.setIcon(R.drawable.ic_baseline_person_24);
                    break;
            }
        }).attach();
    }

}