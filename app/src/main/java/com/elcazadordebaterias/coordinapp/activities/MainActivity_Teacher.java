package com.elcazadordebaterias.coordinapp.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.fragments.AdministrationFragment_Teacher;
import com.elcazadordebaterias.coordinapp.fragments.FilesFragment_Student;
import com.elcazadordebaterias.coordinapp.fragments.FilesFragment_Teacher;
import com.elcazadordebaterias.coordinapp.fragments.GroupsFragment_Student;
import com.elcazadordebaterias.coordinapp.fragments.GroupsFragment_Teacher;
import com.elcazadordebaterias.coordinapp.fragments.HomeFragment_Student;
import com.elcazadordebaterias.coordinapp.fragments.InteractivityFragment_Student;
import com.elcazadordebaterias.coordinapp.fragments.InteractivityFragment_Teacher;
import com.elcazadordebaterias.coordinapp.fragments.ProfileFragment_Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class MainActivity_Teacher extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_teacher);

        // Bottom navigation management
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view_teacher);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_student_home);

    }

    // Static interface to create the fragment associated with the pressed item on the BottomNavigationView
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment;

        int itemId = item.getItemId();

        if (itemId == R.id.nav_teacher_interactivity) {
            selectedFragment = new InteractivityFragment_Teacher();
        } else if (itemId == R.id.nav_teacher_groups) {
            selectedFragment = new GroupsFragment_Teacher();
        } else if (itemId == R.id.nav_teacher_files) {
            selectedFragment = new FilesFragment_Teacher();
        } else if (itemId == R.id.nav_teacher_administration) {
            selectedFragment = new AdministrationFragment_Teacher();
        } else {
            selectedFragment = new AdministrationFragment_Teacher();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_teacher, selectedFragment).commit();

        return true;
    };
}
