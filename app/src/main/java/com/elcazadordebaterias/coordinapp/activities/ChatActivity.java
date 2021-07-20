package com.elcazadordebaterias.coordinapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.MessagesListAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.ChatMessageCard;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Activity that represents a chat room. It is launched when we click on one {@link com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard},
 * and it is assigned to each GroupCard in class {@link com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups.CourseSubjectAdapter}
 *
 * @author Martín Mateos Sánchez
 * @see com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard
 * @see com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups.CourseSubjectAdapter
 */
public class ChatActivity extends AppCompatActivity {

    // Firestore
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    // Views
    private EditText messageInput;
    private MaterialButton sendMessage;
    private MaterialButton sendFile;

    // Reference to chatroom collection
    private CollectionReference chatroomRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatactivity);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Views
        messageInput = findViewById(R.id.messageInput);
        sendMessage = findViewById(R.id.sendMessage);
        sendFile = findViewById(R.id.sendFile);

        // Get card to know where we have to write the messages
        Gson gson = new Gson();
        String cardAsString = getIntent().getStringExtra("cardAsString");
        GroupCard card = gson.fromJson(cardAsString, GroupCard.class);

        // Reference to the collection of the messages
        chatroomRef = fStore
                .collection("CoursesOrganization").document(card.getCourseName())
                .collection("Subjects").document(card.getSubjectName())
                .collection("Groups").document(card.getGroupId())
                .collection("ChatRoom");

        // Recyclerview setup
        RecyclerView messageListContainer = findViewById(R.id.messageListContainer);

        ArrayList<ChatMessageCard> messageList = new ArrayList<ChatMessageCard>();
        MessagesListAdapter messageAdapter = new MessagesListAdapter(messageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        messageListContainer.setAdapter(messageAdapter);
        messageListContainer.setLayoutManager(layoutManager);

        // Listen for added message in the chatroom
        chatroomRef.orderBy("date", Query.Direction.ASCENDING).addSnapshotListener((queryDocumentSnapshots, error) -> {

            if (error != null) {
                return;
            } else if (queryDocumentSnapshots == null) {
                return;
            }

            messageList.clear();
            for (QueryDocumentSnapshot messageDoc : queryDocumentSnapshots) {
                ChatMessageCard message = messageDoc.toObject(ChatMessageCard.class);
                messageList.add(message);
            }
            messageAdapter.notifyDataSetChanged();
        });

        // Setup for send message button
        sendMessage.setOnClickListener(v -> {
            if (!messageInput.getText().toString().isEmpty()) {
                fStore.collection("Students").document(fAuth.getUid()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            sendMessage((String) document.getData().get("FullName"), messageInput.getText().toString(), fAuth.getUid());
                        } else {
                            fStore.collection("Teachers").document(fAuth.getUid()).get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    DocumentSnapshot document1 = task1.getResult();
                                    if (document1.exists()) {
                                        sendMessage((String) document1.getData().get("FullName"), messageInput.getText().toString(), fAuth.getUid());
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        sendMessage.setOnClickListener(v -> {

        });

    }

    private void sendMessage(String fullName, String text, String userId) {
        ChatMessageCard message = new ChatMessageCard(fullName, userId, text, Timestamp.now().toDate());

        chatroomRef.add(message).addOnSuccessListener(documentReference -> messageInput.getText().clear());

    }

}
