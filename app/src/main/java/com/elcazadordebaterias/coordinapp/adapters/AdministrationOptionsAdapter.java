package com.elcazadordebaterias.coordinapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.AdministrationFragment_Teacher_Courses;
import com.elcazadordebaterias.coordinapp.fragments.AdministrationFragment_Teacher_Dates;

/**
 * Class to handle the pages of the viewpager attached at the AdministrationFragment_Teacher fragment.
 *
 * @author Martín Mateos Sánchez
 */
public class AdministrationOptionsAdapter extends FragmentStateAdapter {
    public AdministrationOptionsAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new AdministrationFragment_Teacher_Courses();
            case 1:
                return new AdministrationFragment_Teacher_Dates();
            default:
                return new AdministrationFragment_Teacher_Courses();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // To be changed if the number of tabs increases
    }
}