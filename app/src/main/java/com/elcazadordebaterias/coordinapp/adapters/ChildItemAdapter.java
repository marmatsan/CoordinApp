package com.elcazadordebaterias.coordinapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.ChildItem;

import java.util.List;

public class ChildItemAdapter extends RecyclerView.Adapter<ChildItemAdapter.ChildViewHolder> {

    private List<ChildItem> ChildItemList;

    public ChildItemAdapter(List<ChildItem> childItemList)
    {
        this.ChildItemList = childItemList;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.child_item, viewGroup, false);

        return new ChildViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder childViewHolder, int position) {

        ChildItem childItem = ChildItemList.get(position);

        childViewHolder.ChildItemTitle.setText(childItem.getChildItemTitle());
        childViewHolder.ChildItemEmail.setText(childItem.getChildItemEmail());
    }

    @Override
    public int getItemCount() {

        return ChildItemList.size();
    }
    class ChildViewHolder extends RecyclerView.ViewHolder {

        TextView ChildItemTitle;
        TextView ChildItemEmail;

        ChildViewHolder(View itemView) {
            super(itemView);
            ChildItemTitle = itemView.findViewById(R.id.child_item_title);
            ChildItemEmail = itemView.findViewById(R.id.child_item_email);
        }
    }
}

