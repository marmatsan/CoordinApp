package com.elcazadordebaterias.coordinapp.adapters.recyclerviews;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.interactivity.teacher.InteractivityCardsAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.ChatMessageCard;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firestore.v1.DocumentTransform;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MessagesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MESSAGE_LEFT = 0;
    private static final int MESSAGE_RIGHT = 1;
    private static final int MESSAGE_CENTER = 2;

    private ArrayList<ChatMessageCard> messageList;

    private FirebaseAuth fAuth;

    private Context context;

    public MessagesListAdapter(ArrayList<ChatMessageCard> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;

        fAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            case MESSAGE_LEFT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_chatmessageleft, parent, false);
                return new TextMessageViewHolder(view);
            case MESSAGE_RIGHT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_chatmessageright, parent, false);
                return new TextMessageViewHolder(view);
            default: // MessageCenter
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_chatmessagecenter, parent, false);
                return new MessageWithFileViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        ChatMessageCard messageCard = messageList.get(position);



        if (holder.getItemViewType() == MESSAGE_CENTER) {
            MessageWithFileViewHolder messageHolder = (MessageWithFileViewHolder) holder;
            messageHolder.messageTitle.setText(messageCard.getMessageTitle());
            String uploaderNameStr = "Subido por: "+ messageCard.getSenderName();
            messageHolder.uploaderName.setText(uploaderNameStr);

            String fileNameStr = "Nombre del archivo: "+ messageCard.getFileName();
            messageHolder.fileName.setText(fileNameStr);

            String dateNameStr = "Fecha de subida: "+ messageCard.getStringRepD();
            messageHolder.date.setText(dateNameStr);

            messageHolder.downloadFile.setOnClickListener(v -> {
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(messageCard.getFileRef().getDownloadLink());
                DownloadManager.Request request = new DownloadManager.Request(uri);

                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, messageCard.getFileRef().getFileName());

                downloadManager.enqueue(request);
            });

        } else {
            TextMessageViewHolder messageHolder = (TextMessageViewHolder) holder;
            messageHolder.messageTitle.setText(messageCard.getMessageTitle());
            messageHolder.date.setText(messageCard.getStringRepD());

            if (messageCard.getMessage().startsWith("http")) {
                SpannableString ss = new SpannableString(messageCard.getMessage());
                ClickableSpan span = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        Uri uri = Uri.parse(messageCard.getMessage());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                    }
                };
                ss.setSpan(span, 0, messageCard.getMessage().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                messageHolder.message.setText(ss);
                messageHolder.message.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                messageHolder.message.setText(messageCard.getMessage());
            }
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getFileRef() != null) {
            return MESSAGE_CENTER;
        } else {
            if (messageList.get(position).getSenderId().equals(fAuth.getUid())) {
                return MESSAGE_RIGHT;
            } else {
                return MESSAGE_LEFT;
            }
        }
    }

    static class TextMessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageTitle;
        TextView message;
        TextView date;

        public TextMessageViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            messageTitle = itemView.findViewById(R.id.messageTitle);
            message = itemView.findViewById(R.id.message);
            date = itemView.findViewById(R.id.date);

        }
    }

    public static class MessageWithFileViewHolder extends TextMessageViewHolder {

        TextView uploaderName;
        TextView fileName;

        FloatingActionButton downloadFile;

        public MessageWithFileViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            uploaderName = itemView.findViewById(R.id.uploaderName);
            fileName = itemView.findViewById(R.id.fileName);
            downloadFile = itemView.findViewById(R.id.downloadFile);
        }

    }

}
