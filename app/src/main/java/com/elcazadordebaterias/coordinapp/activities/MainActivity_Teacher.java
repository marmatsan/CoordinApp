package com.elcazadordebaterias.coordinapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.fragments.teacherfragments.administration.AdministrationFragment_Teacher;
import com.elcazadordebaterias.coordinapp.fragments.teacherfragments.files.FilesFragment_Teacher;
import com.elcazadordebaterias.coordinapp.fragments.teacherfragments.groups.GroupsFragment;
import com.elcazadordebaterias.coordinapp.fragments.teacherfragments.interactivity.InteractivityFragment_Teacher;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * The main activity for the teacher.
 * @author Martín Mateos Sánchez
 */
public class MainActivity_Teacher extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_teacher);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Top App Bar management
        Toolbar toolbar = findViewById(R.id.topAppBar);

        fStore.collection("Teachers").document(fAuth.getUid()).get().addOnSuccessListener(documentSnapshot -> { // TODO: Maybe setting the title in asynchronous way may lead to error
            toolbar.setTitle((String) documentSnapshot.getData().get("FullName"));
        });

        setSupportActionBar(toolbar);

        // Bottom navigation management
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view_teacher);
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
        Fragment selectedFragment;
        int itemId = item.getItemId();

        if (itemId == R.id.nav_teacher_interactivity) {
            selectedFragment = new InteractivityFragment_Teacher();
        } else if (itemId == R.id.nav_teacher_groups) {
            selectedFragment = new GroupsFragment();
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
