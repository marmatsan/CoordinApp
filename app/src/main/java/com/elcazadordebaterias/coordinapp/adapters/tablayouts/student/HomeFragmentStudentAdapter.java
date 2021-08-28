package com.elcazadordebaterias.coordinapp.adapters.tablayouts.student;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.commonfragments.Petitions;
import com.elcazadordebaterias.coordinapp.fragments.student.Events.StudentEvents;
import com.elcazadordebaterias.coordinapp.fragments.student.Home;
import com.elcazadordebaterias.coordinapp.fragments.commonfragments.Participants;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;

/**
 * Adapter to handle the pages of the viewpager attached at the
 * {@link Home} fragment.
 *
 *
 * @author Martín Mateos Sánchez
 */
public class HomeFragmentStudentAdapter extends FragmentStateAdapter {

    private String selectedCourse;
    private String selectedSubject;

    public HomeFragmentStudentAdapter(Fragment fragment, String selectedCourse, String selectedSubject) {
        super(fragment);
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new Petitions(UserType.TYPE_STUDENT, selectedCourse, selectedSubject);
            case 2:
                return new StudentEvents(selectedCourse, selectedSubject);
            default:
                return new Participants(UserType.TYPE_STUDENT, selectedCourse, selectedSubject);
        }
    }

    @Override
    public int getItemCount() {
        return 3; // To be changed if the number of tabs increases
    }

}
