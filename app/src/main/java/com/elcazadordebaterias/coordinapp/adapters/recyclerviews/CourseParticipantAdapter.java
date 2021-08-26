package com.elcazadordebaterias.coordinapp.adapters.recyclerviews;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.activities.ChatActivity;
import com.elcazadordebaterias.coordinapp.utils.cards.CourseParticipantCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.google.gson.Gson;

import java.util.List;

public class CourseParticipantAdapter extends RecyclerView.Adapter<CourseParticipantAdapter.CourseParticipantViewHolder> {

    private List<CourseParticipantCard> courseParticipantList;
    private int userType;

    public CourseParticipantAdapter(List<CourseParticipantCard> courseParticipantList, int userType) {
        this.courseParticipantList = courseParticipantList;
        this.userType = userType;
    }

    @NonNull
    @Override
    public CourseParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.utils_cards_courseparticipant, viewGroup, false);

        return new CourseParticipantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseParticipantViewHolder viewHolder, int position) {
        CourseParticipantCard courseParticipant = courseParticipantList.get(position);

        viewHolder.courseparticipant_avatar.setImageResource(courseParticipant.getImage());
        viewHolder.participantRole.setText(courseParticipant.getParticipantRole());
        viewHolder.participantName.setText(courseParticipant.getParticipantName());
        viewHolder.participantEmail.setText(courseParticipant.getParticipantEmail());


        if (userType == UserType.TYPE_TEACHER) {
            viewHolder.view.setOnClickListener(v -> {

            });
        }

    }

    @Override
    public int getItemCount() {
        return courseParticipantList.size();
    }

    static class CourseParticipantViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView courseparticipant_avatar;
        TextView participantRole;
        TextView participantName;
        TextView participantEmail;

        CourseParticipantViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            courseparticipant_avatar = itemView.findViewById(R.id.courseparticipant_avatar);
            participantRole = itemView.findViewById(R.id.courseparticipant_role);
            participantName = itemView.findViewById(R.id.courseparticipant_name);
            participantEmail = itemView.findViewById(R.id.courseparticipant_email);
        }
    }
}

