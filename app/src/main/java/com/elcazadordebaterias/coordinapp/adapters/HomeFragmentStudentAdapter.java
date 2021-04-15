package com.elcazadordebaterias.coordinapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.AdministrationFragment_Teacher_Courses;
import com.elcazadordebaterias.coordinapp.fragments.AdministrationFragment_Teacher_Dates;
import com.elcazadordebaterias.coordinapp.fragments.AdministrationFragment_Teacher_Petitions;
import com.elcazadordebaterias.coordinapp.fragments.HomeFragment_Student;
import com.elcazadordebaterias.coordinapp.fragments.HomeFragment_Student_Courses;
import com.elcazadordebaterias.coordinapp.fragments.HomeFragment_Student_Petitions;

public class HomeFragmentStudentAdapter extends FragmentStateAdapter {

    public HomeFragmentStudentAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new HomeFragment_Student_Petitions();
            default:
                return new HomeFragment_Student_Courses();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // To be changed if the number of tabs increases
    }

}
