package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.files;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.cards.files.FileCard;

import java.util.ArrayList;

public class FilesCardListAdapter extends RecyclerView.Adapter<FilesCardListAdapter.FilesCardViewHolder>{

    private ArrayList<FileCard> filesList;

    public FilesCardListAdapter(ArrayList<FileCard> filesList){
        this.filesList = filesList;
    }

    @NonNull
    @Override
    public FilesCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_filescontainercard, parent, false);

        return new FilesCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesCardViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class FilesCardViewHolder extends RecyclerView.ViewHolder{

        public FilesCardViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
