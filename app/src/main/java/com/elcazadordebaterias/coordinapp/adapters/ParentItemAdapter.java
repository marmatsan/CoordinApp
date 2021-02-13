package com.elcazadordebaterias.coordinapp.adapters;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.ParentItem;

import java.util.List;

public class ParentItemAdapter extends RecyclerView.Adapter<ParentItemAdapter.ParentViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private List<ParentItem> itemList;

    public ParentItemAdapter(List<ParentItem> itemList) {
        this.itemList = itemList;
        for (int i = 0; i < itemList.size(); i++) {
            expandState.append(i, false);
        }
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.parent_item, viewGroup, false);

        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder parentViewHolder, int position) {

        ParentItem parentItem = itemList.get(position);

        parentViewHolder.ParentItemTitle.setText(parentItem.getParentItemTitle());

        LinearLayoutManager layoutManager = new LinearLayoutManager(parentViewHolder.ChildRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);

        layoutManager.setInitialPrefetchItemCount(parentItem.getChildItemList().size());

        ChildItemAdapter childItemAdapter = new ChildItemAdapter(parentItem.getChildItemList());
        parentViewHolder.ChildRecyclerView.setLayoutManager(layoutManager);
        parentViewHolder.ChildRecyclerView.setAdapter(childItemAdapter);
        parentViewHolder.ChildRecyclerView.setRecycledViewPool(viewPool);

        //check if view is expanded
        final boolean isExpanded = expandState.get(position);
        parentViewHolder.expandableView.setVisibility(isExpanded?View.VISIBLE:View.GONE);

        parentViewHolder.arrowBtn.setOnClickListener(view -> {
            onClickButton(parentViewHolder.expandableView,  position);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private void onClickButton(final ConstraintLayout expandableLayout, final  int i) {

        if (expandableLayout.getVisibility() == View.VISIBLE){
            expandableLayout.setVisibility(View.GONE);
            expandState.put(i, false);
        }else{
            expandableLayout.setVisibility(View.VISIBLE);
            expandState.put(i, true);
        }
    }

    static class ParentViewHolder extends RecyclerView.ViewHolder {

        private TextView ParentItemTitle;
        private RecyclerView ChildRecyclerView;
        ConstraintLayout expandableView;
        Button arrowBtn;

        ParentViewHolder(final View itemView) {
            super(itemView);

            ParentItemTitle = itemView.findViewById(R.id.parent_item_title);
            ChildRecyclerView = itemView.findViewById(R.id.child_recyclerview);
            expandableView = itemView.findViewById(R.id.constraintLayout_subjectExpandableView);
            arrowBtn = itemView.findViewById(R.id.subject_expandableButton);
        }
    }
}
