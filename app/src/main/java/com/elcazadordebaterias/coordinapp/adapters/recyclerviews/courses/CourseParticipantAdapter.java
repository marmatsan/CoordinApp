package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.courses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.cards.courses.CourseParticipantCard;

import java.util.List;

/**
 * Adapter to be used in a list made of {@link CourseParticipantCard}. The mentioned list is displayed
 * when we expand a {@link com.elcazadordebaterias.coordinapp.utils.cards.courses.CourseSubjectCard}, and
 * it shows all the participants of the subject (teacher and students).
 *
 * @author Martín Mateos Sánchez
 */
public class CourseParticipantAdapter extends RecyclerView.Adapter<CourseParticipantAdapter.CourseParticipantViewHolder> {

    private List<CourseParticipantCard> courseParticipantList;

    public CourseParticipantAdapter(List<CourseParticipantCard> courseParticipantList) {
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
        CourseParticipantCard courseParticipant = courseParticipantList.get(position);

        viewHolder.participantRole.setText(courseParticipant.getParticipantRole());
        viewHolder.participantName.setText(courseParticipant.getParticipantName());
        viewHolder.participantEmail.setText(courseParticipant.getParticipantEmail());
    }

    @Override
    public int getItemCount() {
        return courseParticipantList.size();
    }

    static class CourseParticipantViewHolder extends RecyclerView.ViewHolder {
        TextView participantRole;
        TextView participantName;
        TextView participantEmail;

        CourseParticipantViewHolder(View itemView) {
            super(itemView);

            participantRole = itemView.findViewById(R.id.courseparticipant_role);
            participantName = itemView.findViewById(R.id.courseparticipant_name);
            participantEmail = itemView.findViewById(R.id.courseparticipant_email);
        }
    }
}

