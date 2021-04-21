package com.elcazadordebaterias.coordinapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.cards.PetitionGroupParticipantCard;

import java.util.ArrayList;

public class ParticipansListViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PetitionGroupParticipantCard> items;

    public ParticipansListViewAdapter(Context context, ArrayList<PetitionGroupParticipantCard> items){
        this.context = context;
        this.items = items;
    }
    @Override
    public int getCount() {
        return this.items.size();
    }
    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v;

        if (view == null){
            LayoutInflater inflater = LayoutInflater.from(this.context);
            v = inflater.inflate(R.layout.utils_groupparticipant,null);
        } else {
            v = view;
        }

        PetitionGroupParticipantCard participant = items.get(position);

        TextView participantName = v.findViewById(R.id.participantName);
        ImageView participantStatus = v.findViewById(R.id.petitionStatusImage);

        participantName.setText(participant.getParticipantName());

        if(participant.getPetitionStatusImage() == 0){
            participantStatus.setImageResource(R.drawable.ic_petitionpending);
        }else if(participant.getPetitionStatusImage() == 1){
            participantStatus.setImageResource(R.drawable.ic_petitionaccepted);
        }else if(participant.getPetitionStatusImage() == 2){
            participantStatus.setImageResource(R.drawable.ic_petitioncanceled);
        }
        return v;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
