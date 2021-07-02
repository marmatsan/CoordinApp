package com.elcazadordebaterias.coordinapp.adapters.tablayouts.teacher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.commonfragments.courses.Courses;
import com.elcazadordebaterias.coordinapp.fragments.teacher.administration.Administration;
import com.elcazadordebaterias.coordinapp.fragments.teacher.administration.Dates;
import com.elcazadordebaterias.coordinapp.fragments.teacher.administration.Petitions;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;

/**
 * Adapter to handle the pages of the viewpager attached at the
 * {@link Administration} fragment.
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
                return new Petitions();
            case 2:
                return new Dates();
            default:
                return new Courses(UserType.TYPE_TEACHER);
        }
    }

    @Override
    public int getItemCount() {
        return 3; // To be changed if the number of tabs increases
    }
}
