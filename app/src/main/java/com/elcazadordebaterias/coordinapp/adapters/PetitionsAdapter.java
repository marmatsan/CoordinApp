 package com.elcazadordebaterias.coordinapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.CardItemRequest;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class PetitionsAdapter extends RecyclerView.Adapter<PetitionsAdapter.PetitionsViewHolder> {

    private ArrayList<CardItemRequest> requests;

    public static class PetitionsViewHolder extends RecyclerView.ViewHolder{

        MaterialButton cancelrequest;
        MaterialButton acceptrequest;

        TextView studentname;
        TextView coursenumber;

        public PetitionsViewHolder(View view){
            super(view);
            cancelrequest = view.findViewById(R.id.cancel_request);
            cancelrequest.setOnClickListener(view1 -> {
                // TODO: Remove card from the recyclerview
            });
            acceptrequest = view.findViewById(R.id.accept_request);
            acceptrequest.setOnClickListener(view2 -> {
                // TODO: Create the group
            });

            studentname = view.findViewById(R.id.studentname);
            coursenumber = view.findViewById(R.id.requestedcourse);
        }

    }

    public PetitionsAdapter(ArrayList<CardItemRequest> requests){
        this.requests = requests;
    }

    @NonNull
    @Override
    public PetitionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_requestsubjectcard, parent, false);
        return new PetitionsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PetitionsViewHolder holder, int position) {
        CardItemRequest currentItem = requests.get(position);

        holder.studentname.setText(currentItem.getStudentName());
        holder.coursenumber.setText(currentItem.getCourseNumber());
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

}
