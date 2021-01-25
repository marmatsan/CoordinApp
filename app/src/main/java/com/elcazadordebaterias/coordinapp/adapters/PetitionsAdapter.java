package com.elcazadordebaterias.coordinapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.google.android.material.button.MaterialButton;

public class PetitionsAdapter extends RecyclerView.Adapter<PetitionsAdapter.PetitionsViewHolder> {

    public static class PetitionsViewHolder extends RecyclerView.ViewHolder{

        MaterialButton cancelrequest;
        MaterialButton acceptrequest;

        TextView studentname;
        TextView coursenumber;

        public PetitionsViewHolder(View view){
            super(view);
            cancelrequest = view.findViewById(R.id.cancel_request);
            acceptrequest = view.findViewById(R.id.accept_request);

            studentname = view.findViewById(R.id.studentname);
            coursenumber = view.findViewById(R.id.requestedcourse);
        }

    }
    // TODO: 26-01-2021 Continue with populating the card of the petition
    @NonNull
    @Override
    public PetitionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_requestsubjectcard, parent, false);
        PetitionsViewHolder pvh = new PetitionsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull PetitionsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
