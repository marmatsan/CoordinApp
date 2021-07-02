package com.elcazadordebaterias.coordinapp.fragments.commonfragments.groups;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.tablayouts.GroupsFragmentAdapter;

import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.elcazadordebaterias.coordinapp.utils.dialogs.commondialogs.CreateGroupDialog;
import com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs.CreateAutomaticDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * The fragment representing the Groups/Chat Tab of the teacher.
 *
 * @author Martín Mateos Sánchez
 */
public class Groups extends Fragment {

    // Firebase
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    // Animations for the buttons
    Animation rotateOpen;
    Animation rotateClose;
    Animation fromBottom;
    Animation toBottom;

    // Views
    private TabLayout tablayout;
    private ViewPager2 viewpager;

    private FloatingActionButton createGroup;
    private FloatingActionButton createAutomaticGroup;
    private FloatingActionButton createManualGroup;

    // Adapter
    private GroupsFragmentAdapter optionsAdapter;

    private final int userType;
    private boolean clicked;

    public Groups(int userType) {
        this.userType = userType;
        clicked = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        rotateOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(getContext(), R.anim.from_botton_anim);
        toBottom = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        createGroup = view.findViewById(R.id.createGroup);
        createAutomaticGroup = view.findViewById(R.id.createAutomaticGroup);
        createManualGroup = view.findViewById(R.id.createManualGroup);


        if (userType == UserType.TYPE_STUDENT) {
            createGroup.setOnClickListener(v -> {
                CreateGroupDialog dialog = new CreateGroupDialog(userType);
                dialog.show(getFragmentManager(), "dialog");
            });
        } else if (userType == UserType.TYPE_TEACHER) {
            createGroup.setOnClickListener(v -> {
                onAddButtonClicked();
            });
        }

        createManualGroup.setOnClickListener(v -> {
            CreateGroupDialog dialog = new CreateGroupDialog(userType);
            dialog.show(getFragmentManager(), "dialog");
        });

        createAutomaticGroup.setOnClickListener(v -> {
            CreateAutomaticDialog dialog = new CreateAutomaticDialog();
            dialog.show(getFragmentManager(), "dialog");
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tablayout = view.findViewById(R.id.tabLayout);
        viewpager = view.findViewById(R.id.fragmentContainer);

        tablayout.setInlineLabel(false);

        optionsAdapter = new GroupsFragmentAdapter(this, userType);
        viewpager.setAdapter(optionsAdapter);

        new TabLayoutMediator(tablayout, viewpager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Grupales");
                    tab.setIcon(R.drawable.ic_group);
                    break;
                case 1:
                    tab.setText("Individuales");
                    tab.setIcon(R.drawable.ic_baseline_person_24);
                    break;
            }
        }).attach();
    }

    private void onAddButtonClicked(){
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setVisibility(boolean clicked){
        if(!clicked){
            createAutomaticGroup.setVisibility(View.VISIBLE);
            createManualGroup.setVisibility(View.VISIBLE);
        } else {
            createAutomaticGroup.setVisibility(View.INVISIBLE);
            createManualGroup.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(boolean clicked){
        if(!clicked) {
            createManualGroup.startAnimation(fromBottom);
            createAutomaticGroup.startAnimation(fromBottom);
            createGroup.startAnimation(rotateOpen);
        } else {
            createManualGroup.startAnimation(toBottom);
            createAutomaticGroup.startAnimation(toBottom);
            createGroup.startAnimation(rotateClose);
        }
    }

}