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
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.CreateGroupItem;

import java.util.ArrayList;

/**
 * Adapter to be used with a popup list in {@link com.elcazadordebaterias.coordinapp.fragments.teacherfragments.groups.GroupsFragment}.
 * It will be used to display a popup list with (by now) two options by the teacher: create a group
 * by hand or create a group automatically. The student does not have access to this list.
 *
 * @author Martín Mateos Sánchez
 */
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
