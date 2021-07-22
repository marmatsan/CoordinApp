package com.elcazadordebaterias.coordinapp.adapters.tablayouts.teacher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.EmptyFragment;
import com.elcazadordebaterias.coordinapp.fragments.commonfragments.courses.Courses;
import com.elcazadordebaterias.coordinapp.fragments.teacher.administration.Administration;
import com.elcazadordebaterias.coordinapp.fragments.teacher.administration.Petitions;
import com.elcazadordebaterias.coordinapp.utils.cards.courses.CourseParticipantCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;

import java.util.ArrayList;

/**
 * Adapter to handle the pages of the viewpager attached at the
 * {@link Administration} fragment.
 *
 *
 * @author Martín Mateos Sánchez
 */
public class AdministrationFragmentTeacherAdapter extends FragmentStateAdapter {

    private String selectedCourse;
    private String selectedSubject;

    public AdministrationFragmentTeacherAdapter(Fragment fragment, String selectedCourse, String selectedSubject) {
        super(fragment);
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new Petitions(selectedCourse, selectedSubject);
            case 2:
                return new EmptyFragment();
            case 3:
                return new EmptyFragment();
            default:
                return new Courses(UserType.TYPE_TEACHER, selectedCourse, selectedSubject);
        }
    }

    @Override
    public int getItemCount() {
        return 4; // To be changed if the number of tabs increases
    }
}
