package com.elcazadordebaterias.coordinapp.adapters.tablayouts.teacher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.teacher.AdministrationFragment;
import com.elcazadordebaterias.coordinapp.fragments.teacher.AdministrationFragment_Courses;
import com.elcazadordebaterias.coordinapp.fragments.teacher.AdministrationFragment_Dates;
import com.elcazadordebaterias.coordinapp.fragments.teacher.AdministrationFragment_Petitions;

/**
 * Adapter to handle the pages of the viewpager attached at the
 * {@link AdministrationFragment} fragment.
 *
 *
 * @author Martín Mateos Sánchez
 */
public class AdministrationFragmentTeacherAdapter extends FragmentStateAdapter {
    public AdministrationFragmentTeacherAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new AdministrationFragment_Petitions();
            case 2:
                return new AdministrationFragment_Dates();
            default:
                return new AdministrationFragment_Courses();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // To be changed if the number of tabs increases
    }
}
