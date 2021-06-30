package com.elcazadordebaterias.coordinapp.adapters.recyclerviews;

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

public class MessagesListAdapter extends RecyclerView.Adapter<MessagesListAdapter.MessageListViewHolder> {

    private static final int MESSAGE_LEFT = 0;
    private static final int MESSAGE_RIGHT = 1;

    private ArrayList<ChatMessageCard> messageList;

    FirebaseAuth fAuth;

    public MessagesListAdapter(ArrayList<ChatMessageCard> messageList) {
        this.messageList = messageList;
        fAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @NotNull
    @Override
    public MessageListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MESSAGE_RIGHT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_chatmessage_right, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_chatmessage_left, parent, false);
        }

        return new MessageListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageListViewHolder holder, int position) {
        ChatMessageCard message = messageList.get(position);

        holder.senderName.setText(message.getSenderName());
        holder.message.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSenderId().equals(fAuth.getUid())){
            return MESSAGE_RIGHT;
        } else {
            return MESSAGE_LEFT;
        }
    }

    static class MessageListViewHolder extends RecyclerView.ViewHolder{

        TextView senderName;
        TextView message;

        public MessageListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            senderName = itemView.findViewById(R.id.senderName);
            message = itemView.findViewById(R.id.message);
        }
    }
}
