package com.elcazadordebaterias.coordinapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.elcazadordebaterias.coordinapp.fragments.FilesFragment_Student;
import com.elcazadordebaterias.coordinapp.fragments.GroupsFragment_Student;
import com.elcazadordebaterias.coordinapp.fragments.HomeFragment_Student;
import com.elcazadordebaterias.coordinapp.fragments.InteractivityFragment_Student;
import com.elcazadordebaterias.coordinapp.fragments.ProfileFragment_Student;
import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.ListPopupWindowAdapter;

import com.elcazadordebaterias.coordinapp.utils.SubjectItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity_Student extends AppCompatActivity {
    private ListPopupWindow listPopupWindow;
    private ArrayList<SubjectItem> mSubjectList;
    private ListPopupWindowAdapter mListPopupWindowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);

        // Bottom navigation management
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view_student);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_student_home);

        // Top App Bar management
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        //ListPopupWindow (subject list)
        mSubjectList = new ArrayList<SubjectItem>();
        mSubjectList.add(new SubjectItem(R.drawable.ic_baseline_maths_24, "Matemáticas"));
        mSubjectList.add(new SubjectItem(R.drawable.ic_baseline_literature_24, "Lengua"));

        mListPopupWindowAdapter = new ListPopupWindowAdapter(this, mSubjectList);

        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(600); //TODO: Change to something better (button.getWidth(), after the app has focus)

        listPopupWindow.setAnchorView(findViewById(R.id.action_subjects));
        listPopupWindow.setAdapter(mListPopupWindowAdapter);
        listPopupWindow.setModal(true); //TODO: Better approach?

    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topappbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_subjects) {
            listPopupWindow.show(); // TODO: Build the listPopupMenu, then show it
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
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