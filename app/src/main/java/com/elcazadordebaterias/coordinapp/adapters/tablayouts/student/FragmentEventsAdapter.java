package com.elcazadordebaterias.coordinapp.adapters.tablayouts.student;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.elcazadordebaterias.coordinapp.fragments.student.Events.SpokersEvents;
import com.elcazadordebaterias.coordinapp.fragments.student.Events.TeachersEvents;

public class FragmentEventsAdapter extends FragmentStateAdapter {

    private String selectedCourse;
    private String selectedSubject;

    public FragmentEventsAdapter(Fragment fragment, String selectedCourse, String selectedSubject) {
        super(fragment);
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new TeachersEvents(selectedCourse, selectedSubject);
            default:
                return new SpokersEvents(selectedCourse, selectedSubject);
        }
    }

    @Override
    public int getItemCount() {
        return 2; // To be changed if the number of tabs increases
    }

}
