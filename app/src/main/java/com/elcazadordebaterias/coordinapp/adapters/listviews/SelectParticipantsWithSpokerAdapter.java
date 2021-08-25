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
            holder.selectSpoker = convertView.findViewById(R.id.radioButton);
            holder.selectSpoker.setVisibility(View.GONE);

            holder.selectParticipant.setOnClickListener(checkbox -> {
                SelectParticipantItemWithSpoker participant = (SelectParticipantItemWithSpoker) holder.selectParticipant.getTag();
                participant.setSelected(!participant.isSelected()); // If the participant is selected, unselect it
                if (participant.isSpoker()) {
                    participant.setSpoker(false);
                }
                notifyDataSetChanged();
            });

            holder.selectSpoker.setOnClickListener(view -> {
                SelectParticipantItemWithSpoker participant = (SelectParticipantItemWithSpoker) holder.selectSpoker.getTag();
                for (SelectParticipantItemWithSpoker item : participantsList) {
                    item.setSpoker(false);
                }
                participant.setSpoker(true);
                notifyDataSetChanged();
            });

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SelectParticipantItemWithSpoker participant = participantsList.get(position);

        holder.participantName.setText(participant.getParticipantName());

        holder.selectParticipant.setTag(participant);
        holder.selectSpoker.setTag(participant);

        holder.selectParticipant.setChecked(participant.isSelected());
        holder.selectSpoker.setChecked(participant.isSpoker());

        holder.selectSpoker.setVisibility(participant.isSelected() && getNumberSelected() > 1 ? View.VISIBLE : View.GONE);

        return convertView;
    }

    static class ViewHolder {
        TextView participantName;
        MaterialCheckBox selectParticipant;
        RadioButton selectSpoker;
    }

    public int getNumberSelected() {
        int selected = 0;

        for (SelectParticipantItemWithSpoker item : participantsList) {
            if (item.isSelected()) {
                selected++;
            }
        }

        return selected;
    }

    public String getSpokerID() {
        String spokerID = null;
        for (SelectParticipantItemWithSpoker item : participantsList) {
            if (item.isSpoker()) {
                spokerID = item.getParticipantId();
                break;
            }
        }
        return spokerID;
    }

    public ArrayList<String> getParticipantsIDs() {
        ArrayList<String> studentsIDs = new ArrayList<String>();
        for (SelectParticipantItemWithSpoker item : participantsList) {
            if (item.isSelected()) {
                studentsIDs.add(item.getParticipantId());
            }
        }
        return studentsIDs;
    }

}
