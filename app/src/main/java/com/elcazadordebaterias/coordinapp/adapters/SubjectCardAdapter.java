package com.elcazadordebaterias.coordinapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.StudentCardItem;

import java.util.ArrayList;

public class SubjectCardAdapter extends RecyclerView.Adapter<SubjectCardAdapter.SubjectCardViewHolder> {

    private ArrayList<StudentCardItem> studentList;

    public static class SubjectCardViewHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        TextView studentEmail;

        public SubjectCardViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.studentName);
            studentEmail = itemView.findViewById(R.id.studentEmail);

        }
    }

    public SubjectCardAdapter(ArrayList<StudentCardItem> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public SubjectCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_studentcard, parent, false);
        SubjectCardViewHolder holder = new SubjectCardViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectCardViewHolder holder, int position) {
        StudentCardItem currentItem = studentList.get(position);

        holder.studentName.setText(currentItem.getStudentName());
        holder.studentEmail.setText(currentItem.getStudentEmail());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }
}
