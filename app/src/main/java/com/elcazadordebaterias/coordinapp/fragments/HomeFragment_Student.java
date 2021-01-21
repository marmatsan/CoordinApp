package com.elcazadordebaterias.coordinapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.RequestSubjectCreationDialog;
import com.google.android.material.button.MaterialButton;

public class HomeFragment_Student extends Fragment {

    MaterialButton addSubject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_home_student, container, false);

        addSubject = rootView.findViewById(R.id.addsubject);
        addSubject.setOnClickListener(view -> {
            requestSubjectCreation();
        });

        return rootView;
    }

    private void requestSubjectCreation(){
        RequestSubjectCreationDialog dialog = new RequestSubjectCreationDialog();
        dialog.show(getFragmentManager(), "dialog");
    }

}