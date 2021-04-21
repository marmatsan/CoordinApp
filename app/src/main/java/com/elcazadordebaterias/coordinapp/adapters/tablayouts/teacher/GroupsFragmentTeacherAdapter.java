package com.elcazadordebaterias.coordinapp.adapters.tablayouts.teacher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.studentfragments.groups.GroupsFragment_SingleChat;
import com.elcazadordebaterias.coordinapp.fragments.teacherfragments.groups.GroupsFragment_GroupalChat;

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
                return new GroupsFragment_SingleChat();
            default:
                return new GroupsFragment_GroupalChat();
        }
    }



    @Override
    public int getItemCount() {
        return 2; // To be changed if the number of tabs increases
    }

}
