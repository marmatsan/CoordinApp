package com.elcazadordebaterias.coordinapp.adapters.listviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.SelectParticipantItem;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

public class SelectParticipantsListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SelectParticipantItem> participantsList;

    public SelectParticipantsListAdapter(Context context, ArrayList<SelectParticipantItem> participantsList) {
        this.context = context;
        this.participantsList = participantsList;
    }

    @Override
    public int getCount() {
        return participantsList.size();
    }

    @Override
    public Object getItem(int position) {
        return participantsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.utils_selectparticipantitem, null);

            holder = new ViewHolder();
            holder.participantName = convertView.findViewById(R.id.participantName);
            holder.selectParticipant = convertView.findViewById(R.id.selectParticipant);

            holder.selectParticipant.setOnClickListener(checkbox -> {
                SelectParticipantItem participant = (SelectParticipantItem) holder.selectParticipant.getTag();
                participant.setSelected(!participant.isSelected()); // If the participant is selected, unselect it
            });

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SelectParticipantItem participant = participantsList.get(position);

        holder.participantName.setText(participant.getParticipantName());
        holder.selectParticipant.setTag(participant);
        holder.selectParticipant.setChecked(participant.isSelected());

        return convertView;
    }

    static class ViewHolder{
        TextView participantName;
        MaterialCheckBox selectParticipant;
    }

}
