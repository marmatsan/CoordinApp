package com.elcazadordebaterias.coordinapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.fragments.EmptyFragment;
import com.elcazadordebaterias.coordinapp.fragments.commonfragments.Interactivity;
import com.elcazadordebaterias.coordinapp.fragments.teacher.administration.Administration;
import com.elcazadordebaterias.coordinapp.fragments.commonfragments.groups.Groups;
import com.elcazadordebaterias.coordinapp.utils.cards.courses.CourseParticipantCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.elcazadordebaterias.coordinapp.utils.dialogs.commondialogs.SelectDisplayedCourse;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Course;
import com.elcazadordebaterias.coordinapp.utils.restmodel.CourseParticipantsIDs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The main activity for the teacher.
 *
 * @author Martín Mateos Sánchez
 */
public class MainActivity_Teacher extends AppCompatActivity implements SelectDisplayedCourse.onSelectedCourse {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private String selectedCourse = "4ºESO B";
    private String selectedSubject = "Matemáticas";

    private MenuItem menuItem;

    private ArrayList<CourseParticipantCard> participants;

    // Toolbar
    Toolbar toolbar;
    String name; // TODO: Just for test

    TextView noCourseSelected;
    FrameLayout fragmentContainer;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_teacher);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Toolbar
        toolbar = findViewById(R.id.topAppBar);

        // Views
        noCourseSelected = findViewById(R.id.noCourseSelected);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        if (selectedCourse == null || selectedSubject == null){
            noCourseSelected.setVisibility(View.VISIBLE);
            fragmentContainer.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);
        }

        fStore.collection("Teachers").document(fAuth.getUid()).get().addOnSuccessListener(documentSnapshot -> { // TODO: Maybe setting the title in asynchronous way may lead to error
            name = (String) documentSnapshot.getData().get("FullName");
            toolbar.setTitle((String) documentSnapshot.getData().get("FullName")+ " | " + selectedCourse +"/"+ selectedSubject);
        });

        setSupportActionBar(toolbar);

        // Initialize array information
        participants = new ArrayList<CourseParticipantCard>();

        // Bottom navigation management
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_teacher_administration);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Static interface to create the fragment associated with the pressed item on the BottomNavigationView
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        this.menuItem = item;

        Fragment selectedFragment;
        int itemId = item.getItemId();

        if (itemId == R.id.nav_teacher_interactivity) {
            selectedFragment = new Interactivity(UserType.TYPE_TEACHER, selectedCourse, selectedSubject);
        } else if (itemId == R.id.nav_teacher_groups) {
            selectedFragment = new Groups(UserType.TYPE_TEACHER, selectedCourse, selectedSubject);
        } else if (itemId == R.id.nav_teacher_files) {
            selectedFragment = new EmptyFragment();
        } else {
            selectedFragment = new Administration(selectedCourse, selectedSubject);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, selectedFragment).commit();

        return true;
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.topappbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.select_course_subject) {
            HashMap<String, ArrayList<String>> detail = new HashMap<String, ArrayList<String>>();
            SelectDisplayedCourse dialog = new SelectDisplayedCourse(detail);
            populateCoursesListAndShow(detail, dialog);
        } else if (item.getItemId() == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSelectedCourseChange(String selectedCourse, String selectedSubject) {
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;

        noCourseSelected.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.VISIBLE);
        bottomNavigationView.setVisibility(View.VISIBLE);

        toolbar.setTitle((String) name+ " | " + selectedCourse +"/"+ selectedSubject);
        navListener.onNavigationItemSelected(menuItem); // Update fragments with the new info
    }

    public void populateCoursesListAndShow(HashMap<String, ArrayList<String>> detail, SelectDisplayedCourse dialog) {
        fStore.collection("CoursesOrganization")
                .whereArrayContains("allParticipantsIDs", fAuth.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                ArrayList<String> subjectNames = new ArrayList<String>();
                detail.put(document.getId(), subjectNames);

                fStore.collection("CoursesOrganization").document(document.getId())
                        .collection("Subjects").whereEqualTo("teacherID", fAuth.getUid())
                        .get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                    for (QueryDocumentSnapshot document1 : queryDocumentSnapshots1) {
                        subjectNames.add(document1.getId());
                    }
                });

            }
            dialog.show(getSupportFragmentManager(), "dialog");
        });
    }

}
