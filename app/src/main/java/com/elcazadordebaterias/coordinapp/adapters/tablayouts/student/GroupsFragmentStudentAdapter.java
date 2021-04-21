package com.elcazadordebaterias.coordinapp.adapters.tablayouts.student;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.studentfragments.groups.GroupsFragment_GroupalChat;
import com.elcazadordebaterias.coordinapp.fragments.studentfragments.groups.GroupsFragment_SingleChat;

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
            return new GroupsFragment_SingleChat();
        }
        return new GroupsFragment_GroupalChat();
    }

    @Override
    public int getItemCount() {
        return 2; // To be changed if the number of tabs increases
    }
}
