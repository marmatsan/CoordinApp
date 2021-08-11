package com.elcazadordebaterias.coordinapp.adapters.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.expandablelistviews.CourseExpandableListAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.CourseParticipantCard;
import com.elcazadordebaterias.coordinapp.utils.cards.TestCard;

import java.util.ArrayList;
import java.util.List;

public class TestCardAdapter extends RecyclerView.Adapter<TestCardAdapter.TestCardViewHolder> {

    private final ArrayList<TestCard> list;

    public TestCardAdapter(ArrayList<TestCard> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public TestCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test_card, viewGroup, false);

        return new TestCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestCardViewHolder holder, int position) {
        TestCard testCard = list.get(position);

        CourseExpandableListAdapter adapter = new CourseExpandableListAdapter(testCard.getHashMap());
        holder.expandableView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class TestCardViewHolder extends RecyclerView.ViewHolder {
        ExpandableListView expandableView;

        TestCardViewHolder(View itemView) {
            super(itemView);

            expandableView = itemView.findViewById(R.id.expandableView);
        }
    }
}