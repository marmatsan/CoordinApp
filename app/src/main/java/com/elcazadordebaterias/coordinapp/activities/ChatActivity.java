package com.elcazadordebaterias.coordinapp.activities;

import android.os.Bundle;
import android.util.Log;
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
 * @see com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard
 * @see com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups.CourseSubjectAdapter
 *
 * @author Martín Mateos Sánchez
 */
public class ChatActivity extends AppCompatActivity {

    // Firestore
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    EditText messageInput;
    MaterialButton sendMessage;

    private String course;
    private String subject;
    private String groupId;

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

        // Initialize vital variables
        course = card.getCourseName();
        subject = card.getSubjectName();
        groupId = card.getGroupId();

        // Reference to the collection of the messages
        CollectionReference chatroomRef = fStore
                .collection("CoursesOrganization").document(course)
                .collection("Subjects").document(subject)
                .collection("Groups").document(groupId)
                .collection("ChatRoom");

        // Recyclerview setup
        RecyclerView messageListContainer = findViewById(R.id.messageListContainer);

        ArrayList<ChatMessageCard> messageList = new ArrayList<ChatMessageCard>();
        MessagesListAdapter messageAdapter = new MessagesListAdapter(messageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        messageListContainer.setAdapter(messageAdapter);
        messageListContainer.setLayoutManager(layoutManager);

        // Initialize message list
        /*
        chatroomRef.orderBy("date", Query.Direction.ASCENDING).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        messageList.add(document.toObject(ChatMessageCard.class));
                    }
                    messageAdapter.notifyDataSetChanged();
                });
         */

        // Listen for new messages
        chatroomRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot queryDocumentSnapshots,
                                @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                if(error != null){
                    return;
                } else if (queryDocumentSnapshots == null){
                    return;
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        messageList.add(dc.getDocument().toObject(ChatMessageCard.class));
                    }
                }
                messageAdapter.notifyDataSetChanged();
            }
        });

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

    }

    private void sendMessage(GroupCard card, String fullName, String text, String uid) {
        ChatMessageCard message = new ChatMessageCard(fullName, uid, text, Timestamp.now().toDate());

        fStore.collection("CoursesOrganization").document(card.getCourseName())
                .collection("Subjects").document(card.getSubjectName())
                .collection("Groups").document(card.getGroupId())
                .collection("ChatRoom").add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                messageInput.getText().clear();
            }
        });

    }

}
