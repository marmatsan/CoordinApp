package com.elcazadordebaterias.coordinapp.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.elcazadordebaterias.coordinapp.fragments.FilesFragment;
import com.elcazadordebaterias.coordinapp.fragments.GroupsFragment;
import com.elcazadordebaterias.coordinapp.fragments.HomeFragment;
import com.elcazadordebaterias.coordinapp.fragments.InteractivityFragment;
import com.elcazadordebaterias.coordinapp.fragments.ProfileFragment;
import com.elcazadordebaterias.coordinapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom navigation management
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_groups); // TODO: Cambiar a nav_home al finalizar desarrollo de Grupos (para que el primer fragment qye se abra al iniciar la aplicación sea home)


        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Static interface to create the fragment associated with the pressed item on the BottomNavigationView
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;

        if (item.getItemId() == R.id.nav_interactivity){
            selectedFragment = new InteractivityFragment();
        }else if (item.getItemId() == R.id.nav_groups){
            selectedFragment = new GroupsFragment();
        }else if (item.getItemId() == R.id.nav_home){
            selectedFragment = new HomeFragment();
        }else if (item.getItemId() == R.id.nav_files){
            selectedFragment = new FilesFragment();
        }else if (item.getItemId() == R.id.nav_profile){
            selectedFragment = new ProfileFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        return true;
    };

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }
}