package com.elcazadordebaterias.coordinapp.adapters.tablayouts.teacher;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.EmptyFragment;
import com.elcazadordebaterias.coordinapp.fragments.commonfragments.Courses;
import com.elcazadordebaterias.coordinapp.fragments.commonfragments.Petitions;
import com.elcazadordebaterias.coordinapp.fragments.teacher.files.GroupalFiles;
import com.elcazadordebaterias.coordinapp.fragments.teacher.files.IndividualFiles;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;

public class FilesFragmentTeacherAdapter extends FragmentStateAdapter {

    private String selectedCourse;
    private String selectedSubject;

    public FilesFragmentTeacherAdapter(Fragment fragment, String selectedCourse, String selectedSubject) {
        super(fragment);
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new IndividualFiles(selectedCourse, selectedSubject);
            default:
                return new GroupalFiles(selectedCourse, selectedSubject);
        }
    }

    @Override
    public int getItemCount() {
        return 2; // To be changed if the number of tabs increases
    }
}