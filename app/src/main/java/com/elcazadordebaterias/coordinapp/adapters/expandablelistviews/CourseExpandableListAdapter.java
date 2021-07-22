package com.elcazadordebaterias.coordinapp.adapters.expandablelistviews;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.elcazadordebaterias.coordinapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Test class to implement a new way of displaying the courses. Currently not used (in development).
 *
 * @author Martín Mateos Sánchez
 */
public class CourseExpandableListAdapter extends BaseExpandableListAdapter {

    private final HashMap<String, ArrayList<String>> data;
    private final ArrayList<String> keySet;

    private OnChildClick onChildClick;

    public CourseExpandableListAdapter(HashMap<String, ArrayList<String>> expandableListDetail, OnChildClick onChildClick) {
        this.data = expandableListDetail;
        this.keySet = new ArrayList<String>();
        this.onChildClick = onChildClick;

        keySet.addAll(data.keySet());
    }

    @Override
    public Object getGroup(int listPosition) {
        return keySet.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_expandablelists_courseheaderitem, null);
        }

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.courseName);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return data.get(keySet.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_expandablelists_subjectheaderitem, null);
        }

        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.subjectName);
        expandedListTextView.setText(expandedListText);

        convertView.setOnClickListener(v -> {
            if(onChildClick != null){
                String selectedCourse, selectedSubject;

                selectedCourse = keySet.get(listPosition);
                selectedSubject = data.get(selectedCourse).get(expandedListPosition);

                onChildClick.onClick(selectedCourse, selectedSubject);
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return data.get(keySet.get(listPosition)).size();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public interface OnChildClick {
        void onClick(String selectedCourse, String selectedSubject);
    }

}
