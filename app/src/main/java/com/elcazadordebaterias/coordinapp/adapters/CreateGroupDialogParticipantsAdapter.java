package com.elcazadordebaterias.coordinapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.CreateGroupDialogSpinnerItem;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupDialogParticipantsAdapter extends ArrayAdapter<CreateGroupDialogSpinnerItem> {

    private final ArrayList<CreateGroupDialogSpinnerItem> participantsList;

    public CreateGroupDialogParticipantsAdapter(Context context, ArrayList<CreateGroupDialogSpinnerItem> participantsList) {
        super(context, 0, participantsList);
        this.participantsList = participantsList;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.utils_creategroupdialog_spinner_item, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.mTextView = view.findViewById(R.id.participantName);
            viewHolder.mCheckBox = view.findViewById(R.id.selectParticipant);

            viewHolder.mCheckBox.setOnClickListener(checkBox -> {
                CreateGroupDialogSpinnerItem element = (CreateGroupDialogSpinnerItem) viewHolder.mCheckBox.getTag();
                if(!element.isTeacher()){
                    element.setSelected(!element.isSelected());
                }else{
                    Toast.makeText(getContext(), "El profesor debe de estar siempre incluido", Toast.LENGTH_LONG).show();
                    viewHolder.mCheckBox.setChecked(true);
                }
            });

            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mCheckBox.setTag(participantsList.get(position));
        viewHolder.mTextView.setText(participantsList.get(position).getParticipantName());
        viewHolder.mCheckBox.setChecked(participantsList.get(position).isSelected());

        return view;
    }

    public static class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}
