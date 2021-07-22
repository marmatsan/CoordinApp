package com.elcazadordebaterias.coordinapp.adapters.tablayouts;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.commonfragments.groups.Groups;
import com.elcazadordebaterias.coordinapp.fragments.commonfragments.groups.GroupalChat;
import com.elcazadordebaterias.coordinapp.fragments.commonfragments.groups.SingleChat;

/**
 * Adapter to handle the pages of the viewpager attached at the
 * {@link Groups} fragment.
 *
 *
 * @author Martín Mateos Sánchez
 */
public class GroupsFragmentAdapter extends FragmentStateAdapter {

    private final int userType;
    private String selectedCourse;
    private String selectedSubject;

    public GroupsFragmentAdapter(Fragment fragment, int userType, String selectedCourse, String selectedSubject) {
        super(fragment);
        this.userType = userType;
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new SingleChat();
            default:
                return new GroupalChat(userType);
        }
    }



    @Override
    public int getItemCount() {
        return 2; // To be changed if the number of tabs increases
    }

}
