package com.elcazadordebaterias.coordinapp.fragments.groups;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups.CourseCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.CourseCard;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.CourseSubjectCard;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupalChat} factory method to
 * create an instance of this fragment.
 */
public class GroupalChat extends Fragment {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private ArrayList<CourseCard> coursesList;
    private ArrayList<CourseSubjectCard> subjectsList;
    private ArrayList<GroupCard> groupsList;

    private CourseCardAdapter coursesAdapter;

    private int userType;

    public GroupalChat(int userType){
        this.userType = userType;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        coursesList = new ArrayList<CourseCard>();
        subjectsList = new ArrayList<CourseSubjectCard>();
        groupsList = new ArrayList<GroupCard>();

        coursesAdapter = new CourseCardAdapter(coursesList, getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups_groupalchat, container, false);

        RecyclerView recyclerviewGroups = view.findViewById(R.id.recyclerviewGroups);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerviewGroups.setAdapter(coursesAdapter);
        recyclerviewGroups.setLayoutManager(layoutManager);

        if(userType == UserType.TYPE_STUDENT) {
            fStore.collectionGroup("Groups").whereArrayContains("participantsIds", fAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    populateGroups(queryDocumentSnapshots);
                }
            });
        } else if (userType == UserType.TYPE_TEACHER) {

        }
        return view;
    }

    private void populateGroups(QuerySnapshot queryDocumentSnapshots){
        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
            Group group = document.toObject(Group.class);
            ArrayList<String> participantNames = new ArrayList<String>();

            for (int i = 0; i < group.getParticipants().size(); i++) {
                participantNames.add(group.getParticipants().get(i).getParticipantFullName());
            }

            groupsList.add(new GroupCard(document.getId(), group.getCourseName(), group.getSubjectName(), participantNames));

        }
    }

    private void shortGroups(){

    }

}