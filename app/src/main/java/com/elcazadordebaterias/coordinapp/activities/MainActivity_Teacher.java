package com.elcazadordebaterias.coordinapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.Toolbar;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.ListPopupWindowAdapter;
import com.elcazadordebaterias.coordinapp.utils.SubjectItem;

import java.util.ArrayList;

public class MainActivity_Teacher extends AppCompatActivity {

    //TODO: 09-01-2021 This is just for developement (remove later). We choose what type of application we want to show (student:user or teacher:admin)
    private ListPopupWindow listPopupWindowUserType;
    private ArrayList<SubjectItem> chooseUserType;
    private ListPopupWindowAdapter chooseUserTypeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_teacher);

        // Top App Bar management
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        
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
                if (chosenItem != null && chosenItem.getSubjectName().equals("Alumno")) {
                    Intent intent = new Intent(view.getContext(), MainActivity_Student.class);
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
            // listPopupWindow.show(); // TODO: Build the listPopupMenu, then show it
        } else if (item.getItemId() == R.id.action_settings) {
            listPopupWindowUserType.show();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
