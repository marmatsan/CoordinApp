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
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class GroupsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private Context mContext;
    private MaterialButton mAddGroup;
    ArrayList<CardviewItem> cardViewItemList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_groups, container, false);

        mRecyclerView = rootView.findViewById(R.id.fragment_groups_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new CardviewAdapter(cardViewItemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAddGroup = rootView.findViewById(R.id.fragment_groups_button_addgroup);
        mAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertItem();
            }
        });

        return rootView;
    }

    public void insertItem(){
        cardViewItemList.add(new CardviewItem(new ExpandableListView(mContext)));
        mAdapter.notifyItemInserted(cardViewItemList.size() - 1);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

}