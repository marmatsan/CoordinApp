package com.elcazadordebaterias.coordinapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.GroupParticipant;

import java.util.ArrayList;

public class PetitionGroupCardParticipantsListAdapter extends ArrayAdapter<GroupParticipant> {

    private ArrayList<GroupParticipant> participantsList;

    public PetitionGroupCardParticipantsListAdapter(@NonNull Context context, @NonNull ArrayList<GroupParticipant> participantsList) {
        super(context, 0, participantsList);
        this.participantsList = participantsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;

        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.utils_groupparticipant, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.mParticipantName = view.findViewById(R.id.participantName);
            viewHolder.mPetitionStatusImage = view.findViewById(R.id.petitionStatusImage);

            view.setTag(viewHolder);

        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mParticipantName.setText(participantsList.get(position).getParticipantName());
        viewHolder.mPetitionStatusImage.setImageResource(participantsList.get(position).getPetitionStatusImage());

        return view;
    }

    @Override
    public boolean isEnabled(int position) { // No item of this list is clickable
        return false;
    }

    public static class ViewHolder{
        private TextView mParticipantName;
        private ImageView mPetitionStatusImage;
    }

}
