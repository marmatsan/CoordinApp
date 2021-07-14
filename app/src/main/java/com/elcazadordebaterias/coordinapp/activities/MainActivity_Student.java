package com.elcazadordebaterias.coordinapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.fragments.EmptyFragment;
import com.elcazadordebaterias.coordinapp.fragments.commonfragments.Interactivity;
import com.elcazadordebaterias.coordinapp.fragments.commonfragments.groups.Groups;
import com.elcazadordebaterias.coordinapp.fragments.student.home.Home;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
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

        // Toolbar
        Toolbar toolbar = findViewById(R.id.topAppBar);

        fStore.collection("Students").document(fAuth.getUid()).get().addOnSuccessListener(documentSnapshot -> { // TODO: Maybe setting the title in asynchronous way may lead to error
            toolbar.setTitle((String) documentSnapshot.getData().get("FullName"));
        });

        setSupportActionBar(toolbar);
    }

    // Static interface to create the fragment associated with the pressed item on the BottomNavigationView
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment;

        int itemId = item.getItemId();

        if (itemId == R.id.nav_student_interactivity) {
            selectedFragment = new Interactivity(UserType.TYPE_STUDENT);
        } else if (itemId == R.id.nav_student_groups) {
            selectedFragment = new Groups(UserType.TYPE_STUDENT);
        } else if (itemId == R.id.nav_student_home) {
            selectedFragment = new Home();
        } else if (itemId == R.id.nav_student_files) {
            selectedFragment = new EmptyFragment();
        } else if (itemId == R.id.nav_student_profile) {
            selectedFragment = new EmptyFragment();
        } else {
            selectedFragment = new Home();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_student, selectedFragment).commit();

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

        if(item.getItemId() == R.id.menu_logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}