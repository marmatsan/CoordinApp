package com.elcazadordebaterias.coordinapp.fragments.studentfragments.groups;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.tablayouts.student.GroupsFragmentStudentAdapter;
import com.elcazadordebaterias.coordinapp.utils.dialogs.student.CreateGroupDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * The fragment representing the Groups/Chat Tab of the student.
 * @author Martín Mateos Sánchez
 */
public class GroupsFragment extends Fragment {

    private MaterialButton mGroupCreationRequest;

    private GroupsFragmentStudentAdapter studentAdapter;

    private TabLayout tablayout;
    private ViewPager2 viewpager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_groups_student, container, false);

        mGroupCreationRequest = rootView.findViewById(R.id.groupCreationRequest);
        mGroupCreationRequest.setOnClickListener(view -> {
           CreateGroupDialog dialog = new CreateGroupDialog();
           dialog.show(getFragmentManager(), "dialog");
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tablayout = view.findViewById(R.id.tabLayout);
        tablayout.setInlineLabel(false);
        viewpager = view.findViewById(R.id.fragment_container);

        studentAdapter = new GroupsFragmentStudentAdapter(this);
        viewpager.setAdapter(studentAdapter);

        new TabLayoutMediator(tablayout, viewpager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Chats grupales");
                    tab.setIcon(R.drawable.ic_baseline_group_24);
                    break;
                case 1:
                    tab.setText("Chats con el profesor");
                    tab.setIcon(R.drawable.ic_baseline_person_24);
                    break;
            }
        }).attach();
    }

}