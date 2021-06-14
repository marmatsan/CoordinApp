package com.elcazadordebaterias.coordinapp.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.dialogs.student.CreateGroupDialog;

/**
 * Activity that represents a chat room. It is launched when we click on one {@link com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard},
 * and it is assigned to each GroupCard in class {@link com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups.CourseSubjectAdapter}
 *
 * @see com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard
 * @see com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups.CourseSubjectAdapter
 *
 * @author Martín Mateos Sánchez
 */
public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatactivity);

    }
}
