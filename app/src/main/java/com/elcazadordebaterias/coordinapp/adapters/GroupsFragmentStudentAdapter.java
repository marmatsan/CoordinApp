package com.elcazadordebaterias.coordinapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.AdministrationFragment_Teacher_Courses;
import com.elcazadordebaterias.coordinapp.fragments.AdministrationFragment_Teacher_Dates;
import com.elcazadordebaterias.coordinapp.fragments.AdministrationFragment_Teacher_Petitions;
import com.elcazadordebaterias.coordinapp.fragments.GroupsFragment_Student_GroupalChat;
import com.elcazadordebaterias.coordinapp.fragments.GroupsFragment_Student_SingleChat;

/**
 * Class to handle the pages of the viewpager attached at the AdministrationFragment_Teacher fragment.
 *
 * @author Martín Mateos Sánchez
 */
public class GroupsFragmentStudentAdapter extends FragmentStateAdapter {
    public GroupsFragmentStudentAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new GroupsFragment_Student_SingleChat();
        }
        return new GroupsFragment_Student_GroupalChat();
    }

    @Override
    public int getItemCount() {
        return 2; // To be changed if the number of tabs increases
    }
}
