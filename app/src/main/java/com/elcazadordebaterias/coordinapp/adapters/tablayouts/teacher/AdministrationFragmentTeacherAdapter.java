package com.elcazadordebaterias.coordinapp.adapters.tablayouts.teacher;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.EmptyFragment;
import com.elcazadordebaterias.coordinapp.fragments.commonfragments.Participants;
import com.elcazadordebaterias.coordinapp.fragments.teacher.administration.Administration;
import com.elcazadordebaterias.coordinapp.fragments.commonfragments.Petitions;
import com.elcazadordebaterias.coordinapp.fragments.teacher.administration.TeacherEvents;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;


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
                return new Petitions(UserType.TYPE_TEACHER, selectedCourse, selectedSubject);
            case 2:
                return new TeacherEvents(selectedCourse, selectedSubject);
            default:
                return new Participants(UserType.TYPE_TEACHER, selectedCourse, selectedSubject);
        }
    }

    @Override
    public int getItemCount() {
        return 3; // To be changed if the number of tabs increases
    }
}
