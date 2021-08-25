package com.elcazadordebaterias.coordinapp.adapters.listviews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.SelectParticipantItemWithSpoker;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

public class SelectParticipantsWithSpokerAdapter extends BaseAdapter {

    private Context context;
    private final ArrayList<SelectParticipantItemWithSpoker> participantsList;

    public SelectParticipantsWithSpokerAdapter(Context context, ArrayList<SelectParticipantItemWithSpoker> participantsList) {
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

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.utils_selectparticipantwithspokertitem, null);

            holder = new ViewHolder();
            holder.participantName = convertView.findViewById(R.id.participantName);
            holder.selectParticipant = convertView.findViewById(R.id.selectParticipant);
            holder.radioButton = convertView.findViewById(R.id.radioButton);

            holder.selectParticipant.setOnClickListener(checkbox -> {
                SelectParticipantItemWithSpoker participant = (SelectParticipantItemWithSpoker) holder.selectParticipant.getTag();
                participant.setSelected(!participant.isSelected()); // If the participant is selected, unselect it
                Log.d("DEBUGGING", "Checked");
            });

            holder.radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SelectParticipantItemWithSpoker participant = (SelectParticipantItemWithSpoker) holder.selectParticipant.getTag();
                for(SelectParticipantItemWithSpoker item : participantsList) {
                    item.setSpoker(false);
                    Log.d("DEBUGGING", "Selected spokesman");
                }
                participant.setSpoker(true);
            });

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SelectParticipantItemWithSpoker participant = participantsList.get(position);

        holder.participantName.setText(participant.getParticipantName());
        holder.selectParticipant.setTag(participant);
        holder.selectParticipant.setChecked(participant.isSelected());
        holder.radioButton.setChecked(participant.isSpoker());

        return convertView;
    }

    static class ViewHolder {
        TextView participantName;
        MaterialCheckBox selectParticipant;
        RadioButton radioButton;
    }

}
