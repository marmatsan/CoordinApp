package com.elcazadordebaterias.coordinapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.PetitionsAdapter;
import com.elcazadordebaterias.coordinapp.utils.CardItemRequest;
import com.elcazadordebaterias.coordinapp.utils.FirebaseRequestInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdministrationFragment_Teacher_Courses extends Fragment {

    RecyclerView petitionscontainer;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_administration_teacher_courses, container, false);

        petitionscontainer = v.findViewById(R.id.petitionscontainer);
        layoutManager = new LinearLayoutManager(getContext());
        petitionscontainer.setLayoutManager(layoutManager);

        Map<String, Object> firebaseRequests = new HashMap<String, Object>();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        ArrayList<CardItemRequest> requestList = new ArrayList<CardItemRequest>();

        adapter = new PetitionsAdapter(requestList);
        petitionscontainer.setAdapter(adapter);

        fStore.collection("Requests")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            firebaseRequests.put(document.getId(), document.toObject(FirebaseRequestInfo.class));
                        }

                        for (Map.Entry<String, Object> entry : firebaseRequests.entrySet()) {
                            FirebaseRequestInfo info = (FirebaseRequestInfo) entry.getValue();

                            CardItemRequest newRequest = new CardItemRequest(info.getStudentName(), info.getCourseNumber() + " " + info.getCourseNumberLetter());
                            requestList.add(newRequest);
                        }
                        adapter.notifyDataSetChanged();
                    } // TODO: 26-01-2021 Check if task fails
                });

        return v;
    }
}