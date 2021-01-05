package com.elcazadordebaterias.coordinapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.elcazadordebaterias.coordinapp.R;

import java.util.ArrayList;
import java.util.List;

public class ListPopupWindowAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<String> mDataSource;
    private LayoutInflater layoutInflater;

    public ListPopupWindowAdapter(Activity activity, List<String> dataSource){
        this.mActivity = activity;
        this.mDataSource = dataSource;
        layoutInflater = mActivity.getLayoutInflater();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.list_popup_window_item, null);
            holder.subject_icon = (ImageView) convertView.findViewById(R.id.subject_icon);
            holder.subject_name = (TextView) convertView.findViewById(R.id.subject_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        // bind data
        holder.subject_icon.setImageResource(R.drawable.ic_baseline_maths_24);
        holder.subject_name.setText(getItem(position));
        return convertView;
    }

    public class ViewHolder{
        private ImageView subject_icon;
        private TextView subject_name;
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public String getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
