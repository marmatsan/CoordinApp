package com.elcazadordebaterias.coordinapp.fragments.teacherfragments.groups;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.expandablelistviews.CourseExpandableListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupsFragment_GroupalChat} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment_GroupalChat extends Fragment {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups_teacher_groupalchat, container, false);

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

        return v;
    }

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

}