package com.elcazadordebaterias.coordinapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.elcazadordebaterias.coordinapp.fragments.FilesFragment;
import com.elcazadordebaterias.coordinapp.fragments.GroupsFragment;
import com.elcazadordebaterias.coordinapp.fragments.HomeFragment;
import com.elcazadordebaterias.coordinapp.fragments.InteractivityFragment;
import com.elcazadordebaterias.coordinapp.fragments.ProfileFragment;
import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.ListPopupWindowAdapter;

import com.elcazadordebaterias.coordinapp.utils.SubjectItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity_Student extends AppCompatActivity {
    private ListPopupWindow listPopupWindow;
    private ArrayList<SubjectItem> mSubjectList;
    private ListPopupWindowAdapter mListPopupWindowAdapter;

    //TODO: 06-01-2021 This is just for developement (remove later). We choose what type of application we want to show (student:user or teacher:admin)
    private ListPopupWindow listPopupWindowUserType;
    private ArrayList<SubjectItem> chooseUserType;
    private ListPopupWindowAdapter chooseUserTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);

        // Bottom navigation management
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
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

        /*
         * TODO: 06-01-2021 Listpopupwindow to choose what type of application we want to show (student:user or teacher:admin). Remove later
         */
        chooseUserType = new ArrayList<SubjectItem>();
        chooseUserType.add(new SubjectItem(R.drawable.ic_baseline_person_24, "Alumno"));
        chooseUserType.add(new SubjectItem(R.drawable.ic_baseline_account_box_24, "Profesor"));

        chooseUserTypeAdapter = new ListPopupWindowAdapter(this, chooseUserType);

        listPopupWindowUserType = new ListPopupWindow(this);
        listPopupWindowUserType.setWidth(600);

        listPopupWindowUserType.setAnchorView(findViewById(R.id.action_settings));
        listPopupWindowUserType.setAdapter(chooseUserTypeAdapter);
        listPopupWindowUserType.setModal(true);

        listPopupWindowUserType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubjectItem chosenItem = (SubjectItem) parent.getItemAtPosition(position);
                if (chosenItem != null && chosenItem.getSubjectName().equals("Profesor")) {
                    Intent intent = new Intent(view.getContext(), MainActivity_Teacher.class);
                    startActivity(intent); // TODO: 06-01-2021 Return to student app from teacher app
                }
            }
        });
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
        } else if (item.getItemId() == R.id.action_settings) {
            listPopupWindowUserType.show();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    // Static interface to create the fragment associated with the pressed item on the BottomNavigationView
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;

        if (item.getItemId() == R.id.nav_student_interactivity) {
            selectedFragment = new InteractivityFragment();
        } else if (item.getItemId() == R.id.nav_student_groups) {
            selectedFragment = new GroupsFragment();
        } else if (item.getItemId() == R.id.nav_student_home) {
            selectedFragment = new HomeFragment();
        } else if (item.getItemId() == R.id.nav_student_files) {
            selectedFragment = new FilesFragment();
        } else if (item.getItemId() == R.id.nav_student_profile) {
            selectedFragment = new ProfileFragment();
        } else {
            selectedFragment = new HomeFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_student, selectedFragment).commit();

        return true;
    };
}