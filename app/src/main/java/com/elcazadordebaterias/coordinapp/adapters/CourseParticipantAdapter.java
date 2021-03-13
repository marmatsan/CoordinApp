package com.elcazadordebaterias.coordinapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.CourseParticipant;

import java.util.List;

public class CourseParticipantAdapter extends RecyclerView.Adapter<CourseParticipantAdapter.CourseParticipantViewHolder> {

    private List<CourseParticipant> courseParticipantList;

    public CourseParticipantAdapter(List<CourseParticipant> courseParticipantList) {
        this.courseParticipantList = courseParticipantList;
    }

    @NonNull
    @Override
    public CourseParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.utils_courseparticipant, viewGroup, false);

        return new CourseParticipantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseParticipantViewHolder viewHolder, int position) {
        CourseParticipant courseParticipant = courseParticipantList.get(position);

        viewHolder.participantName.setText(courseParticipant.getParticipantName());
        viewHolder.participantEmail.setText(courseParticipant.getParticipantEmail());
    }

    @Override
    public int getItemCount() {
        return courseParticipantList.size();
    }

    static class CourseParticipantViewHolder extends RecyclerView.ViewHolder {
        TextView participantName;
        TextView participantEmail;

        CourseParticipantViewHolder(View itemView) {
            super(itemView);

            participantName = itemView.findViewById(R.id.courseparticipant_name);
            participantEmail = itemView.findViewById(R.id.courseparticipant_email);
        }
    }
}

