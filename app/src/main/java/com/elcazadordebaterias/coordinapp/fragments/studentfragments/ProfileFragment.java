package com.elcazadordebaterias.coordinapp.fragments.studentfragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.activities.LoginActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

/**
 * The fragment representing the Profile Tab of the student.
 * @author Martín Mateos Sánchez
 */
public class ProfileFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_student, container, false);
    }
}