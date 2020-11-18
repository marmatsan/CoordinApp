package com.elcazadordebaterias.coordinapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class GroupsFragment extends Fragment {

    private ArrayList<CardviewItem> mCardviewList;
    private MaterialButton mAddGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCardviewList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_groups, container, false);

        mAddGroup = rootView.findViewById(R.id.fragment_groups_button_addgroup);
        mAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpandableListView expandableListView = null; // TODO: Create the expandable listview
                mCardviewList.add(new CardviewItem(R.drawable.ic_baseline_chat_24, expandableListView));
            }
        });

        return rootView;
    }
}