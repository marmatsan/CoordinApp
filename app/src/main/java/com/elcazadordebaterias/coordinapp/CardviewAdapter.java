package com.elcazadordebaterias.coordinapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardviewAdapter extends RecyclerView.Adapter<CardviewAdapter.CardviewHolder> {
    private ArrayList<CardviewItem> mCardviewList;

    public static class CardviewHolder extends RecyclerView.ViewHolder{
        public ExpandableListView mExpandableListView;

        public CardviewHolder(View view){
            super(view);
            mExpandableListView = view.findViewById(R.id.item_cardview_expandablelistview);
        }

    }

    public CardviewAdapter(ArrayList<CardviewItem> cardviewList) {
        mCardviewList = cardviewList;
    }

    @NonNull
    @Override
    public CardviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, parent, false);
        return new CardviewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardviewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mCardviewList.size();
    }

}
