package com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Adapter to be used in a list made of {@link GroupCard}. The mentioned list is displayed
 * when we expand a {@link com.elcazadordebaterias.coordinapp.utils.cards.groups.CourseSubjectCard}, and
 * it shows all the groups the logged user is in in the selected subject. When clicked in one of the {@link GroupCard}
 * in the mentioned list, the {@link com.elcazadordebaterias.coordinapp.activities.ChatActivity} is launched, opening
 * the associated chat room of this group.
 *
 * @author Martín Mateos Sánchez
 */
public class GroupCardAdapter extends RecyclerView.Adapter<GroupCardAdapter.GroupCardViewHolder> {

    ArrayList<GroupCard> groupsList;
    Context context;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private OnItemClickListener listener;
    private final int userType;

    public GroupCardAdapter(ArrayList<GroupCard> groupsList, Context context, int userType){
        this.groupsList = groupsList;
        this.context = context;
        this.userType = userType;

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public GroupCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.utils_groupcard, parent, false);
        return new GroupCardViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupCardViewHolder holder, int position) {
        GroupCard group = groupsList.get(position);

        holder.groupName.setText(group.getGroupName());

        // A student can't have permits to delete a group
        if (userType == UserType.TYPE_STUDENT) {
            holder.deleteGroup.setVisibility(View.GONE);
        }

        holder.showParticipants.setOnClickListener(v -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
            builderSingle.setTitle("Participantes");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.utils_participantname, R.id.participantName, group.getParticipantNames()){
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
            fStore.collection("CoursesOrganization")
                    .document(group.getCourseName())
                    .collection("Subjects")
                    .document(group.getSubjectName())
                    .collection("Groups")
                    .document(group.getGroupId())
                    .delete().addOnSuccessListener(aVoid -> {

                groupsList.remove(position);
                notifyDataSetChanged();

            });
        });

    }

    @Override
    public int getItemCount() {
        return groupsList.size();
    }

    public interface OnItemClickListener{
        void onItemClicked(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    static class GroupCardViewHolder extends RecyclerView.ViewHolder{
        TextView groupName;
        MaterialButton showParticipants;
        MaterialButton deleteGroup;

        GroupCardViewHolder(View view, OnItemClickListener listener){
            super(view);

            groupName = view.findViewById(R.id.groupName);
            showParticipants = view.findViewById(R.id.showParticipants);
            deleteGroup = view.findViewById(R.id.deleteGroup);

            view.setOnClickListener(view1 -> {
                if(listener != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onItemClicked(position);
                    }
                }
            });

        }
    }
}

