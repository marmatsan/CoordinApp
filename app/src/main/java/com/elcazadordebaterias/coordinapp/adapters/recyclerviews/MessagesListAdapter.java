package com.elcazadordebaterias.coordinapp.adapters.recyclerviews;

import android.app.DownloadManager;
import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.cards.ChatMessageCard;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Adapter to be used by chatactivity to manage the messages
 *
 */
public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.MessageListViewHolder> {

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
    public MessageListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == MESSAGE_RIGHT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_chatmessageright, parent, false);
        } else if (viewType == MESSAGE_LEFT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_chatmessageleft, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_chatmessagecenter, parent, false);
        }

        return new MessageListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageListViewHolder holder, int position) {
        ChatMessageCard messageCard = messageList.get(position);

        holder.messageTitle.setText(messageCard.getMessageTitle());

        if (messageCard.getFileRef() == null){
            holder.message.setText(messageCard.getMessage());
        } else {
            SpannableString ss = new SpannableString(messageCard.getMessage());
            ClickableSpan span = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(messageCard.getFileRef().getDownloadLink());
                    DownloadManager.Request request = new DownloadManager.Request(uri);

                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, messageCard.getFileRef().getFileName());

                    downloadManager.enqueue(request);
                }
            };
            ss.setSpan(span, messageCard.getMessage().indexOf("link"), messageCard.getMessage().indexOf("link") + "link".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.message.setText(ss);
            holder.message.setMovementMethod(LinkMovementMethod.getInstance());
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getFileRef() != null){
            return MESSAGE_CENTER;
        } else {
            if (messageList.get(position).getSenderId().equals(fAuth.getUid())){
                return MESSAGE_RIGHT;
            } else {
                return MESSAGE_LEFT;
            }
        }
    }

    static class MessageListViewHolder extends RecyclerView.ViewHolder{

        TextView messageTitle;
        TextView message;

        public MessageListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            messageTitle = itemView.findViewById(R.id.messageTitle);
            message = itemView.findViewById(R.id.message);
        }
    }
}
