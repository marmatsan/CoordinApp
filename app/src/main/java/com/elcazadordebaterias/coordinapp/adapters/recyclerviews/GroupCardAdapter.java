package com.elcazadordebaterias.coordinapp.adapters.recyclerviews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.activities.ChatActivity;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;

public class GroupCardAdapter extends RecyclerView.Adapter<GroupCardAdapter.GroupCardViewHolder> {

    ArrayList<GroupCard> groupsList;
    Context context;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private final int userType;

    public GroupCardAdapter(ArrayList<GroupCard> groupsList, Context context, int userType) {
        this.groupsList = groupsList;
        this.context = context;
        this.userType = userType;

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public GroupCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_cards_groupcard, parent, false);
        return new GroupCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupCardViewHolder holder, int position) {
        GroupCard groupCard = groupsList.get(position);

        holder.groupName.setText(groupCard.getGroupName());

        // A student can't have permits to delete a group
        if (userType == UserType.TYPE_STUDENT) {
            holder.deleteGroup.setVisibility(View.GONE);
        }

        holder.showParticipants.setOnClickListener(v -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
            builderSingle.setTitle("Participantes");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.utils_participantname, R.id.participantName, groupCard.getParticipantNames()) {
                @Override
                public boolean isEnabled(int position1) {
                    return false;
                }
            };

            builderSingle.setNegativeButton("Vale", (dialog, which) -> dialog.dismiss());
            builderSingle.setAdapter(arrayAdapter, null);
            builderSingle.show();
        });

        holder.deleteGroup.setOnClickListener(v -> {

            DocumentReference groupRef = fStore
                    .collection("CoursesOrganization")
                    .document(groupCard.getCourseName())
                    .collection("Subjects")
                    .document(groupCard.getSubjectName())
                    .collection(groupCard.getCollectionId())
                    .document(groupCard.getGroupId());

            if (groupCard.getCollectionId().equals("IndividualGroups")) {
                groupRef.update("hasVisibility", false);
            } else {
                groupRef.collection("ChatRoomWithTeacher").get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots1) {
                        document.getReference().delete();
                    }

                    groupRef.collection("StorageWithTeacher").get().addOnSuccessListener(queryDocumentSnapshots2 -> {
                        for (DocumentSnapshot document : queryDocumentSnapshots2) {
                            document.getReference().delete();
                        }
                        groupRef.collection("ChatRoomWithoutTeacher").get().addOnSuccessListener(queryDocumentSnapshots3 -> {
                            for (DocumentSnapshot document : queryDocumentSnapshots3) {
                                document.getReference().delete();
                            }

                            groupRef.collection("StorageWithoutTeacher").get().addOnSuccessListener(queryDocumentSnapshots4 -> {
                                for (DocumentSnapshot document : queryDocumentSnapshots4) {
                                    document.getReference().delete();
                                }
                                groupRef.collection("InteractivityCards").get().addOnSuccessListener(queryDocumentSnapshots5 -> {
                                    for (DocumentSnapshot document : queryDocumentSnapshots5) {
                                        document.getReference().delete();
                                    }
                                    groupRef.delete();
                                });
                            });
                        });
                    });
                });
            }

        });

        holder.view.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);

            // Convert the GroupCard to JSON to send it to ChatActivity
            Gson gson = new Gson();
            String cardAsString = gson.toJson(groupCard);
            intent.putExtra("cardAsString", cardAsString);
            intent.putExtra("userType", userType);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    static class GroupCardViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView groupName;
        MaterialButton showParticipants;
        MaterialButton deleteGroup;

        GroupCardViewHolder(View view) {
            super(view);
            this.view = view;
            groupName = view.findViewById(R.id.groupName);
            showParticipants = view.findViewById(R.id.showParticipants);
            deleteGroup = view.findViewById(R.id.deleteGroup);
        }
    }

}

