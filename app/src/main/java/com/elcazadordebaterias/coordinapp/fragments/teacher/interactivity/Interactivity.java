package com.elcazadordebaterias.coordinapp.fragments.teacher.interactivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs.CreateInputTextCardDialog;
import com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs.CreateMultichoiceCardDialog;
import com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs.CreateReminderCardDialog;
import com.elcazadordebaterias.coordinapp.utils.utilities.ButtonAnimator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class Interactivity extends Fragment {

    // Firestore
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private String selectedCourse;
    private String selectedSubject;

    public Interactivity(String selectedCourse, String selectedSubject){
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_interactivity, container, false);

        // Buttons
        FloatingActionButton createInteractivityCard = view.findViewById(R.id.createInteractivityCard);
        FloatingActionButton createInputTextCard = view.findViewById(R.id.createInputTextCard);
        FloatingActionButton createMultichoiceCard = view.findViewById(R.id.createMultichoiceCard);
        FloatingActionButton createReminderCard = view.findViewById(R.id.createReminderCard);

        ArrayList<FloatingActionButton> buttons = new ArrayList<FloatingActionButton>();
        buttons.add(createInputTextCard);
        buttons.add(createMultichoiceCard);
        buttons.add(createReminderCard);

        ButtonAnimator buttonAnimator = new ButtonAnimator(getContext(), createInteractivityCard, buttons);

        // Buttons listeners
        createInteractivityCard.setOnClickListener(v -> buttonAnimator.onButtonClicked());

        createInputTextCard.setOnClickListener(v -> {
            CreateInputTextCardDialog dialog = new CreateInputTextCardDialog(selectedCourse, selectedSubject);
            dialog.show(getParentFragmentManager(), "dialog");
        });

        createMultichoiceCard.setOnClickListener(v -> {
            CreateMultichoiceCardDialog dialog = new CreateMultichoiceCardDialog();
            dialog.show(getParentFragmentManager(), "dialog");
        });

        createReminderCard.setOnClickListener(v -> {
            CreateReminderCardDialog dialog = new CreateReminderCardDialog();
            dialog.show(getParentFragmentManager(), "dialog");
        });

        return view;
    }
}