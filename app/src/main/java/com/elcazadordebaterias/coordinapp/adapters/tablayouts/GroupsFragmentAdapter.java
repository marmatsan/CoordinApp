package com.elcazadordebaterias.coordinapp.adapters.tablayouts;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.GroupsFragment_GroupalChat;
import com.elcazadordebaterias.coordinapp.fragments.GroupsFragment_SingleChat;

/**
 * Adapter to handle the pages of the viewpager attached at the
 * {@link com.elcazadordebaterias.coordinapp.fragments.GroupsFragment} fragment.
 *
 *
 * @author Martín Mateos Sánchez
 */
public class GroupsFragmentAdapter extends FragmentStateAdapter {
    public GroupsFragmentAdapter(Fragment fragment) {
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
