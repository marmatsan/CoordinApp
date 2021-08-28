package com.elcazadordebaterias.coordinapp.adapters.listviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.SelectGroupItem;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.SelectParticipantItem;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

public class SelectGroupsItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SelectGroupItem> groupsList;

    public SelectGroupsItemAdapter(Context context, ArrayList<SelectGroupItem> groupsList) {
        this.context = context;
        this.groupsList = groupsList;
    }

    @Override
    public int getCount() {
        return groupsList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.utils_selectparticipantitem, null);

            holder = new ViewHolder();
            holder.participantName = convertView.findViewById(R.id.participantName);
            holder.selectParticipant = convertView.findViewById(R.id.selectParticipant);

            holder.selectParticipant.setOnClickListener(checkbox -> {
                SelectGroupItem group = (SelectGroupItem) holder.selectParticipant.getTag();
                group.setSelected(!group.isSelected()); // If the participant is selected, unselect it
            });

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SelectGroupItem participant = groupsList.get(position);
        holder.participantName.setText(participant.getGroupName());
        holder.selectParticipant.setTag(participant);
        holder.selectParticipant.setChecked(participant.isSelected());

        return convertView;
    }

    static class ViewHolder {
        TextView participantName;
        MaterialCheckBox selectParticipant;
    }

}
