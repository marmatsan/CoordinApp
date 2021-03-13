package com.elcazadordebaterias.coordinapp.adapters;

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
import com.elcazadordebaterias.coordinapp.utils.CourseSubject;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CourseSubjectAdapter extends RecyclerView.Adapter<CourseSubjectAdapter.CourseSubjectViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private List<CourseSubject> mCourseSubjectsList;

    public CourseSubjectAdapter(List<CourseSubject> courseSubjectList) {
        this.mCourseSubjectsList = courseSubjectList;
        for (int i = 0; i < courseSubjectList.size(); i++) {
            expandState.append(i, false);
        }
    }

    @NonNull
    @Override
    public CourseSubjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.utils_coursesubject, viewGroup, false);

        return new CourseSubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseSubjectViewHolder viewHolder, int position) {
        CourseSubject courseSubject = mCourseSubjectsList.get(position);

        viewHolder.subjectName.setText(courseSubject.getSubjectName());

        LinearLayoutManager layoutManager = new LinearLayoutManager(viewHolder.participantsRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);

        layoutManager.setInitialPrefetchItemCount(courseSubject.getCourseParticipantList().size());

        CourseParticipantAdapter courseParticipantAdapter = new CourseParticipantAdapter(courseSubject.getCourseParticipantList());
        viewHolder.participantsRecyclerView.setLayoutManager(layoutManager);
        viewHolder.participantsRecyclerView.setAdapter(courseParticipantAdapter);
        viewHolder.participantsRecyclerView.setRecycledViewPool(viewPool);

        final boolean isExpanded = expandState.get(position); //Check if the view is expanded
        viewHolder.expandableView.setVisibility(isExpanded?View.VISIBLE:View.GONE);

        viewHolder.expandableParticipantsButton.setOnClickListener(view -> {
            if (viewHolder.expandableView.getVisibility() == View.VISIBLE){
                viewHolder.expandableView.setVisibility(View.GONE);
                viewHolder.expandableParticipantsButton.setText(R.string.expandir);
                expandState.put(position, false);
            }else{
                viewHolder.expandableView.setVisibility(View.VISIBLE);
                viewHolder.expandableParticipantsButton.setText(R.string.colapsar);
                expandState.put(position, true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourseSubjectsList.size();
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

    static class CourseSubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;
        MaterialButton expandableParticipantsButton;
        ConstraintLayout expandableView;
        RecyclerView participantsRecyclerView;

        CourseSubjectViewHolder(final View itemView) {
            super(itemView);

            subjectName = itemView.findViewById(R.id.coursesubject_name);
            expandableParticipantsButton = itemView.findViewById(R.id.coursesubject_expandparticipantslistbutton);
            expandableView = itemView.findViewById(R.id.coursesubject_expandableview);
            participantsRecyclerView = itemView.findViewById(R.id.coursesubject_participantsrecyclerview);
        }
    }
}
