package com.elcazadordebaterias.coordinapp.adapters.tablayout.teacher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.teacherfragments.AdministrationFragment_Teacher_Courses;
import com.elcazadordebaterias.coordinapp.fragments.teacherfragments.AdministrationFragment_Teacher_Petitions;
import com.elcazadordebaterias.coordinapp.fragments.teacherfragments.GroupsFragment_Teacher_GroupalChat;
import com.elcazadordebaterias.coordinapp.fragments.teacherfragments.GroupsFragment_Teacher_SingleChat;

/**
 * Class to handle the pages of the viewpager attached at the AdministrationFragment_Teacher fragment.
 *
 * @author Martín Mateos Sánchez
 */
public class GroupsFragmentTeacherAdapter extends FragmentStateAdapter {
    public GroupsFragmentTeacherAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new GroupsFragment_Teacher_GroupalChat();
            default:
                return new GroupsFragment_Teacher_SingleChat();
        }
    }

    @Override
    public int getItemCount() {
        return 2; // To be changed if the number of tabs increases
    }
}
