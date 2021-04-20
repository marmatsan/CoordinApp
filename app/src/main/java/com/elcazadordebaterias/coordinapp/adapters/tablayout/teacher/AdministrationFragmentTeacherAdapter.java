package com.elcazadordebaterias.coordinapp.adapters.tablayout.teacher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.teacherfragments.AdministrationFragment_Teacher_Courses;
import com.elcazadordebaterias.coordinapp.fragments.teacherfragments.AdministrationFragment_Teacher_Dates;
import com.elcazadordebaterias.coordinapp.fragments.teacherfragments.AdministrationFragment_Teacher_Petitions;

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
                return new AdministrationFragment_Teacher_Petitions();
            case 2:
                return new AdministrationFragment_Teacher_Dates();
            default:
                return new AdministrationFragment_Teacher_Courses();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // To be changed if the number of tabs increases
    }
}
