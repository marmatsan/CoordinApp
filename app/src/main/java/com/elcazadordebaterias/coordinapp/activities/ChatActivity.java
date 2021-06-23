package com.elcazadordebaterias.coordinapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.MessagesListAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.ChatMessage;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.elcazadordebaterias.coordinapp.utils.dialogs.student.CreateGroupDialog;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Activity that represents a chat room. It is launched when we click on one {@link com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard},
 * and it is assigned to each GroupCard in class {@link com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups.CourseSubjectAdapter}
 *
 * @see com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard
 * @see com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups.CourseSubjectAdapter
 *
 * @author Martín Mateos Sánchez
 */
public class ChatActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    EditText messageInput;
    MaterialButton sendMessage;

    ArrayList<ChatMessage> messageList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatactivity);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Views
        messageInput = findViewById(R.id.messageInput);
        sendMessage = findViewById(R.id.sendMessage);

        // Get card to know where we have to write the messages
        Gson gson = new Gson();
        String cardAsString = getIntent().getStringExtra("cardAsString");
        GroupCard card = gson.fromJson(cardAsString, GroupCard.class);

        // Recyclerview setup
        RecyclerView messageListContainer = findViewById(R.id.messageListContainer);

        ArrayList<ChatMessage> messageList = new ArrayList<ChatMessage>();
        MessagesListAdapter messageAdapter = new MessagesListAdapter(messageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        messageListContainer.setAdapter(messageAdapter);
        messageListContainer.setLayoutManager(layoutManager);

        // Setup for send message button

        sendMessage.setOnClickListener(v ->
                fStore.collection("CoursesOrganization").document(card.getCourseName())
                .collection("Subjects").document(card.getSubjectName())
                .collection("Groups").document(card.getGroupId())
                .collection("ChatRooms"));

        // Send messa

    }
}
