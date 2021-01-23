package com.elcazadordebaterias.coordinapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.utils.CardviewItem;
import com.elcazadordebaterias.coordinapp.R;

import java.util.ArrayList;

/**
 * Adapter to handle the cardviews that represent the folder of chats with the classmates
 * and with the teachers. When clicked, if there only exists one chat between classmates, the chat
 * will be opened right away, and if there is also a chat with the teacher, it will open another view
 * to choose thet chat we want to open (to be implemented).
 *
 * @author Martín Mateos Sánchez
 */
public class CardviewAdapter extends RecyclerView.Adapter<CardviewAdapter.CardviewHolder> {
    private ArrayList<CardviewItem> mCardviewList;

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
    public void onBindViewHolder(@NonNull CardviewHolder holder, int position) { }

    @Override
    public int getItemCount() {
        return mCardviewList.size();
    }

    public static class CardviewHolder extends RecyclerView.ViewHolder{
        public ExpandableListView mExpandableListView;

        public CardviewHolder(View view){
            super(view);
            mExpandableListView = view.findViewById(R.id.item_cardview_expandablelistview);
        }

    }

}
