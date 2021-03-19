package com.elcazadordebaterias.coordinapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.elcazadordebaterias.coordinapp.utils.CardviewItem;
import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.CardviewAdapter;
import com.elcazadordebaterias.coordinapp.utils.CreateGroupDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

/**
 * The fragment representing the Groups/Chat Tab of the student.
 * @author Martín Mateos Sánchez
 */
public class GroupsFragment_Student extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Context mContext;
    private MaterialButton mGroupCreationRequest;
    ArrayList<CardviewItem> cardViewItemList = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_groups_student, container, false);

        mRecyclerView = rootView.findViewById(R.id.groupsRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new CardviewAdapter(cardViewItemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mGroupCreationRequest = rootView.findViewById(R.id.groupCreationRequest);
        mGroupCreationRequest.setOnClickListener(view -> {
            CreateGroupDialog dialog = new CreateGroupDialog();
            dialog.show(getFragmentManager(), "dialog");
        });

        return rootView;
    }

    public void insertItem(){
        cardViewItemList.add(new CardviewItem(new ExpandableListView(mContext)));
        mAdapter.notifyItemInserted(cardViewItemList.size() - 1);
    }

}