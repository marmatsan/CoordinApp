package com.elcazadordebaterias.coordinapp.adapters;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.ParentParentItem;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ParentParentItemAdapter extends RecyclerView.Adapter<ParentParentItemAdapter.ParentParentViewHolder> {

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private List<ParentParentItem> itemList;

    public ParentParentItemAdapter(List<ParentParentItem> itemList) {
        this.itemList = itemList;
        for (int i = 0; i < itemList.size(); i++) {
            expandState.append(i, false);
        }
    }

    @NonNull
    @Override
    public ParentParentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.parentparent_item, viewGroup, false);

        return new ParentParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentParentViewHolder parentParentViewHolder, int position) {

        ParentParentItem parentParentItem = itemList.get(position);

        parentParentViewHolder.groupName.setText(parentParentItem.getParentParentItemTitle());

        LinearLayoutManager layoutManager = new LinearLayoutManager(parentParentViewHolder.recyclerView_Groups.getContext(), LinearLayoutManager.VERTICAL, false);

        layoutManager.setInitialPrefetchItemCount(parentParentItem.getParentItemList().size());
        ParentItemAdapter parentItemAdapter = new ParentItemAdapter(parentParentItem.getParentItemList());
        parentParentViewHolder.recyclerView_Groups.setLayoutManager(layoutManager);
        parentParentViewHolder.recyclerView_Groups.setAdapter(parentItemAdapter);
        parentParentViewHolder.recyclerView_Groups.setRecycledViewPool(viewPool);

        //check if view is expanded
        final boolean isExpanded = expandState.get(position);
        parentParentViewHolder.constraintLayout_groupsExpandableView.setVisibility(isExpanded?View.VISIBLE:View.GONE);

        parentParentViewHolder.groups_expandableButton.setOnClickListener(view -> {
            if (parentParentViewHolder.constraintLayout_groupsExpandableView.getVisibility() == View.VISIBLE){
                parentParentViewHolder.constraintLayout_groupsExpandableView.setVisibility(View.GONE);
                parentParentViewHolder.groups_expandableButton.setText(R.string.expandir);
                expandState.put(position, false);
            }else{
                parentParentViewHolder.constraintLayout_groupsExpandableView.setVisibility(View.VISIBLE);
                parentParentViewHolder.groups_expandableButton.setText(R.string.colapsar);
                expandState.put(position, true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    static class ParentParentViewHolder extends RecyclerView.ViewHolder {

        TextView groupName;
        RecyclerView recyclerView_Groups;
        ConstraintLayout constraintLayout_groupsExpandableView;
        MaterialButton groups_expandableButton;

        ParentParentViewHolder(final View itemView) {
            super(itemView);

            groupName = itemView.findViewById(R.id.groupName);
            recyclerView_Groups = itemView.findViewById(R.id.recyclerView_Groups);
            constraintLayout_groupsExpandableView = itemView.findViewById(R.id.constraintLayout_groupsExpandableView);
            groups_expandableButton = itemView.findViewById(R.id.groups_expandableButton);

        }
    }
}
