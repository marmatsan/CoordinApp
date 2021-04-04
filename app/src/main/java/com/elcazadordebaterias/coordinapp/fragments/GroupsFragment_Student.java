package com.elcazadordebaterias.coordinapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.CreateGroupDialog;
import com.google.android.material.button.MaterialButton;

/**
 * The fragment representing the Groups/Chat Tab of the student.
 * @author Martín Mateos Sánchez
 */
public class GroupsFragment_Student extends Fragment {

    private MaterialButton mGroupCreationRequest;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_groups_student, container, false);

        mGroupCreationRequest = rootView.findViewById(R.id.groupCreationRequest);
        mGroupCreationRequest.setOnClickListener(view -> {
            CreateGroupDialog dialog = new CreateGroupDialog();
            dialog.show(getFragmentManager(), "dialog");
        });

        return rootView;
    }

}