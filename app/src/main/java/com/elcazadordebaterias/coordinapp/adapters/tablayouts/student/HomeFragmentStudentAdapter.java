package com.elcazadordebaterias.coordinapp.adapters.tablayouts.student;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.studentfragments.home.HomeFragment_Courses;
import com.elcazadordebaterias.coordinapp.fragments.studentfragments.home.HomeFragment_Petitions;

public class HomeFragmentStudentAdapter extends FragmentStateAdapter {

    public HomeFragmentStudentAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new HomeFragment_Petitions();
            default:
                return new HomeFragment_Courses();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // To be changed if the number of tabs increases
    }

}
