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

    /*

        ExpandableListView expandableListView;
        ExpandableListAdapter expandableListAdapter;
        ArrayList<String> expandableListTitle;
        HashMap<String, ArrayList<String>> expandableListData;

        expandableListView = (ExpandableListView) v.findViewById(R.id.courses_list);
        expandableListData = ExpandableListDataPump.getData();

        expandableListTitle = new ArrayList<String>(expandableListData.keySet());
        expandableListAdapter = new CourseExpandableListAdapter(expandableListTitle, expandableListData);

        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListData.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });

            private static class ExpandableListDataPump {
        public static HashMap<String, ArrayList<String>> getData() {
            HashMap<String, ArrayList<String>> expandableListDetail = new HashMap<String, ArrayList<String>>();

            ArrayList<String> cricket = new ArrayList<String>();
            cricket.add("India");
            cricket.add("Pakistan");
            cricket.add("Australia");
            cricket.add("England");
            cricket.add("South Africa");

            ArrayList<String> football = new ArrayList<String>();
            football.add("Brazil");
            football.add("Spain");
            football.add("Germany");
            football.add("Netherlands");
            football.add("Italy");

            ArrayList<String> basketball = new ArrayList<String>();
            basketball.add("United States");
            basketball.add("Spain");
            basketball.add("Argentina");
            basketball.add("France");
            basketball.add("Russia");

            expandableListDetail.put("CRICKET TEAMS", cricket);
            expandableListDetail.put("FOOTBALL TEAMS", football);
            expandableListDetail.put("BASKETBALL TEAMS", basketball);
            return expandableListDetail;
        }
    }

    */

public class CourseExpandableListAdapter extends BaseExpandableListAdapter {

    private ArrayList<String> expandableListTitle;
    private HashMap<String, ArrayList<String>> expandableListDetail;

    public CourseExpandableListAdapter(ArrayList<String> expandableListTitle, HashMap<String, ArrayList<String>> expandableListDetail) {
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
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
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).get(expandedListPosition);
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
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}
