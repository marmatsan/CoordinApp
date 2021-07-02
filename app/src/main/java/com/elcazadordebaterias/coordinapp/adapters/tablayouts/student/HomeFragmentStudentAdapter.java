package com.elcazadordebaterias.coordinapp.adapters.tablayouts.student;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.student.home.Home;
import com.elcazadordebaterias.coordinapp.fragments.commonfragments.courses.Courses;
import com.elcazadordebaterias.coordinapp.fragments.student.home.Petitions;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;

/**
 * Adapter to handle the pages of the viewpager attached at the
 * {@link Home} fragment.
 *
 *
 * @author Martín Mateos Sánchez
 */
public class HomeFragmentStudentAdapter extends FragmentStateAdapter {

    public HomeFragmentStudentAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new Petitions();
            default:
                return new Courses(UserType.TYPE_STUDENT);
        }
    }

    @Override
    public int getItemCount() {
        return 2; // To be changed if the number of tabs increases
    }

}
