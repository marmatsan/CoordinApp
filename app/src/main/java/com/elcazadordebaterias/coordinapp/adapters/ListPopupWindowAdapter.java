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

import com.elcazadordebaterias.coordinapp.utils.SubjectItem;

import java.util.ArrayList;

import com.elcazadordebaterias.coordinapp.R;

public class ListPopupWindowAdapter extends ArrayAdapter<SubjectItem> {

    public ListPopupWindowAdapter(Context context, ArrayList<SubjectItem> subjectList) {
        super(context, 0, subjectList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_popup_window_item, parent, false);
        }

        ImageView subjectIcon = convertView.findViewById(R.id.subject_icon);
        TextView subjectName = convertView.findViewById(R.id.subject_name);

        SubjectItem currentItem = getItem(position);

        subjectIcon.setImageResource(currentItem.getSubjectIcon());
        subjectName.setText(currentItem.getSubjectName());

        return convertView;
    }
}