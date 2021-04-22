package com.elcazadordebaterias.coordinapp.adapters.tablayouts.teacher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.teacherfragments.administration.AdministrationFragment_Courses;
import com.elcazadordebaterias.coordinapp.fragments.teacherfragments.administration.AdministrationFragment_Dates;
import com.elcazadordebaterias.coordinapp.fragments.teacherfragments.administration.AdministrationFragment_Petitions;

/**
 * Class to handle the pages of the viewpager attached at the AdministrationFragment_Teacher fragment.
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
