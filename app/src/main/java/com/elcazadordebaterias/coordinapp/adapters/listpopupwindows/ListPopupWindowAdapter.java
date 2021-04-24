package com.elcazadordebaterias.coordinapp.adapters.listpopupwindows;

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
import com.elcazadordebaterias.coordinapp.utils.CreateGroupItem;

import java.util.ArrayList;

public class ListPopupWindowAdapter extends ArrayAdapter<CreateGroupItem> {

    public ListPopupWindowAdapter(Context context, ArrayList<CreateGroupItem> menuItemsList) {
        super(context, 0, menuItemsList);
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.utils_creategroupitem, parent, false);
        }

        ImageView itemIcon = convertView.findViewById(R.id.itemIcon);
        TextView itemText = convertView.findViewById(R.id.itemText);

        CreateGroupItem currentItem = getItem(position);

        itemIcon.setImageResource(currentItem.getSubjectIcon());
        itemText.setText(currentItem.getSubjectName());

        return convertView;
    }
}
