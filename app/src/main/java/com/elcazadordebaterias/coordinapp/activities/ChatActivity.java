package com.elcazadordebaterias.coordinapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.MessagesListAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.ChatMessage;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.elcazadordebaterias.coordinapp.utils.dialogs.student.CreateGroupDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

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

        sendMessage.setOnClickListener(v -> {
            if(!messageInput.getText().toString().isEmpty()){
                fStore.collection("Students").document(fAuth.getUid()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            sendMessage(card,(String) document.getData().get("FullName"), messageInput.getText().toString(), fAuth.getUid());
                        } else {
                            fStore.collection("Teachers").document(fAuth.getUid()).get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    DocumentSnapshot document1 = task1.getResult();
                                    if (document1.exists()) {
                                        sendMessage(card,(String) document1.getData().get("FullName"), messageInput.getText().toString(), fAuth.getUid());
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        // Initialize message list
        fStore.collection("CoursesOrganization").document(card.getCourseName())
                .collection("Subjects").document(card.getSubjectName())
                .collection("Groups").document(card.getGroupId())
                .collection("ChatRoom")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            messageList.add(document.toObject(ChatMessage.class));
                        }
                        messageAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void sendMessage(GroupCard card, String fullName, String text, String uid) {
        ChatMessage message = new ChatMessage(fullName, uid, text);

        fStore.collection("CoursesOrganization").document(card.getCourseName())
                .collection("Subjects").document(card.getSubjectName())
                .collection("Groups").document(card.getGroupId())
                .collection("ChatRoom").add(message);

    }

}
