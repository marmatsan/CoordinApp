package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups.CourseSubjectAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.CourseCard;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to be used in a list made of {@link CourseCard}.
 * The mentioned list displays all of the courses the logger user is enrolled in.
 *
 * @author Martín Mateos Sánchez
 */
public class CourseCardAdapter extends RecyclerView.Adapter<CourseCardAdapter.CourseCardViewHolder> {

    private final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private ArrayList<CourseCard> coursesList;
    private Context context;
    private final int userType;

    public CourseCardAdapter(ArrayList<CourseCard> coursesList, Context context, int userType) {
        this.coursesList = coursesList;
        this.context = context;
        this.userType = userType;

        for (int i = 0; i < coursesList.size(); i++) {
            expandState.append(i, false);
        }
    }

    @NonNull
    @Override
    public CourseCardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.utils_coursecard, viewGroup, false);

        return new CourseCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseCardViewHolder viewHolder, int position) {

        CourseCard course = coursesList.get(position);

        viewHolder.courseName.setText(course.getCourseName());

        LinearLayoutManager layoutManager = new LinearLayoutManager(viewHolder.subjectsRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);

        layoutManager.setInitialPrefetchItemCount(course.getSubjectList().size());
        CourseSubjectAdapter courseSubjectAdapter = new CourseSubjectAdapter(course.getSubjectList(), context, userType);

        viewHolder.subjectsRecyclerView.setLayoutManager(layoutManager);
            viewHolder.subjectsRecyclerView.setAdapter(courseSubjectAdapter);
        viewHolder.subjectsRecyclerView.setRecycledViewPool(viewPool);

        final boolean isExpanded = expandState.get(position); //Check if the view is expanded
        viewHolder.expandableView.setVisibility(isExpanded?View.VISIBLE:View.GONE);

        viewHolder.expandSubjectsList.setOnClickListener(view -> {
            if (viewHolder.expandableView.getVisibility() == View.VISIBLE){
                viewHolder.expandableView.setVisibility(View.GONE);
                viewHolder.expandSubjectsList.setText(R.string.expandir);
                expandState.put(position, false);
            }else{
                viewHolder.expandableView.setVisibility(View.VISIBLE);
                viewHolder.expandSubjectsList.setText(R.string.colapsar);
                expandState.put(position, true);
            }
        });

    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    static class CourseCardViewHolder extends RecyclerView.ViewHolder {

        TextView courseName;
        MaterialButton expandSubjectsList;
        ConstraintLayout expandableView;
        RecyclerView subjectsRecyclerView;

        CourseCardViewHolder(final View itemView) {
            super(itemView);

            courseName = itemView.findViewById(R.id.courseName);
            expandSubjectsList = itemView.findViewById(R.id.expandSubjectsList);
            expandableView = itemView.findViewById(R.id.expandableView);
            subjectsRecyclerView = itemView.findViewById(R.id.subjectsRecyclerView);

        }
    }
}
