package com.elcazadordebaterias.coordinapp.fragments.teacherfragments.groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.listpopupwindows.ListPopupWindowAdapter;
import com.elcazadordebaterias.coordinapp.adapters.tablayouts.teacher.GroupsFragmentTeacherAdapter;
import com.elcazadordebaterias.coordinapp.utils.CreateGroupItem;
import com.elcazadordebaterias.coordinapp.utils.dialogs.teacher.CreateAutomaticDialog;
import com.elcazadordebaterias.coordinapp.utils.dialogs.teacher.CreateGroupDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

/**
 * The fragment representing the Groups/Chat Tab of the teacher.
 * @author Martín Mateos Sánchez
 */
public class GroupsFragment extends Fragment {
    private GroupsFragmentTeacherAdapter optionsAdapter;

    private ViewPager2 viewpager;
    private TabLayout tablayout;
    private MaterialButton createGroup;

    private ListPopupWindow listPopupWindow;
    private ListPopupWindowAdapter popupWindowAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groups_teacher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tablayout = view.findViewById(R.id.tabLayout);
        viewpager = view.findViewById(R.id.fragment_groups_container);

        tablayout.setInlineLabel(false);

        ArrayList<CreateGroupItem> createGroupMenu = new ArrayList<CreateGroupItem>();
        createGroupMenu.add(new CreateGroupItem(R.drawable.people, "Crear un grupo seleccionando los participantes"));
        createGroupMenu.add(new CreateGroupItem(R.drawable.usersfolder, "Crear un conjunto de grupos de forma aleatoria"));

        popupWindowAdapter  = new ListPopupWindowAdapter(getContext(), createGroupMenu);

        listPopupWindow = new ListPopupWindow(getContext());
        listPopupWindow.setWidth(1000);

        listPopupWindow.setOnItemClickListener((parent, view1, position, id) -> {
            if(position == 0){
                CreateGroupDialog dialog = new CreateGroupDialog();
                dialog.show(getFragmentManager(), "dialog");
            }else{
                CreateAutomaticDialog dialog = new CreateAutomaticDialog();
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        createGroup = view.findViewById(R.id.createGroup);
        listPopupWindow.setAnchorView(createGroup);
        listPopupWindow.setAdapter(popupWindowAdapter);
        listPopupWindow.setModal(true);

        createGroup.setOnClickListener(v -> {
            listPopupWindow.show();
        });

        optionsAdapter = new GroupsFragmentTeacherAdapter(this);
        viewpager.setAdapter(optionsAdapter);


        new TabLayoutMediator(tablayout, viewpager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Profesor/a");
                    tab.setIcon(R.drawable.ic_group);
                    break;
                case 1:
                    tab.setText("Individuales");
                    tab.setIcon(R.drawable.ic_baseline_person_24);
                    break;
            }
        }).attach();
    }

}