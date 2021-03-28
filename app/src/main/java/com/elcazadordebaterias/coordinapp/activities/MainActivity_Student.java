package com.elcazadordebaterias.coordinapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.fragments.FilesFragment_Student;
import com.elcazadordebaterias.coordinapp.fragments.GroupsFragment_Student;
import com.elcazadordebaterias.coordinapp.fragments.HomeFragment_Student;
import com.elcazadordebaterias.coordinapp.fragments.InteractivityFragment_Student;
import com.elcazadordebaterias.coordinapp.fragments.ProfileFragment_Student;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * The main activity for the student.
 *
 * @author Martín Mateos Sánchez
 */
public class MainActivity_Student extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Bottom navigation management
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view_student);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_student_home);

        // Top App Bar management
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
    }

    // Static interface to create the fragment associated with the pressed item on the BottomNavigationView
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment;

        int itemId = item.getItemId();

        if (itemId == R.id.nav_student_interactivity) {
            selectedFragment = new InteractivityFragment_Student();
        } else if (itemId == R.id.nav_student_groups) {
            selectedFragment = new GroupsFragment_Student();
        } else if (itemId == R.id.nav_student_home) {
            selectedFragment = new HomeFragment_Student();
        } else if (itemId == R.id.nav_student_files) {
            selectedFragment = new FilesFragment_Student();
        } else if (itemId == R.id.nav_student_profile) {
            selectedFragment = new ProfileFragment_Student();
        } else {
            selectedFragment = new HomeFragment_Student();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_student, selectedFragment).commit();

        return true;
    };

}