package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.files;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.cards.files.FileCard;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FilesCardListAdapter extends RecyclerView.Adapter<FilesCardListAdapter.FilesCardViewHolder>{

    private ArrayList<FileCard> filesList;
    private Context context;

    public FilesCardListAdapter(ArrayList<FileCard> filesList, Context context){
        this.filesList = filesList;
        this.context = context;
    }

    @NonNull
    @Override
    public FilesCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_files_filecard, parent, false);

        return new FilesCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilesCardViewHolder holder, int position) {
        FileCard fileCard = filesList.get(position);

        if (fileCard.getFileName().endsWith(".pdf")){
            holder.fileImage.setImageResource(R.drawable.ic_pdf_file);
        } else if (fileCard.getFileName().endsWith(".jpg") || fileCard.getFileName().endsWith(".jpeg")|| fileCard.getFileName().endsWith(".png")) {
            holder.fileImage.setImageResource(R.drawable.ic_image);
        } else {
            holder.fileImage.setImageResource(R.drawable.ic_file);
        }

        holder.fileName.setText(fileCard.getFileName());
        holder.uploaderName.setText(fileCard.getUploaderName());
        holder.uploadedDate.setText(fileCard.getUploadedDate());

        holder.downloadButton.setOnClickListener(v -> {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(fileCard.getDownloadLink());
            DownloadManager.Request request = new DownloadManager.Request(uri);

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileCard.getFileName());

            downloadManager.enqueue(request);
        });

    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    static class FilesCardViewHolder extends RecyclerView.ViewHolder{

        ImageView fileImage;
        TextView fileName;
        TextView uploaderName;
        TextView uploadedDate;
        FloatingActionButton downloadButton;

        public FilesCardViewHolder(@NonNull View itemView) {
            super(itemView);

            fileImage = itemView.findViewById(R.id.fileImage);
            fileName = itemView.findViewById(R.id.fileName);
            uploaderName = itemView.findViewById(R.id.uploaderName);
            uploadedDate = itemView.findViewById(R.id.uploadedDate);
            downloadButton = itemView.findViewById(R.id.downloadButton);

        }
    }

}
