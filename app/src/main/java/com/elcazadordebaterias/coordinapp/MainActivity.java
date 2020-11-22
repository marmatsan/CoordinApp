package com.elcazadordebaterias.coordinapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom navigation management
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_groups); // TODO: Cambiar a nav_home al finalizar desarrollo de Grupos (para que el primer fragment qye se abra al iniciar la aplicación sea home)

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
}