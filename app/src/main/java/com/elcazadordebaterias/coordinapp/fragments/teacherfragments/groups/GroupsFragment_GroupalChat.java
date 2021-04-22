package com.elcazadordebaterias.coordinapp.fragments.teacherfragments.groups;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.groups.CourseCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.Group;
import com.elcazadordebaterias.coordinapp.utils.GroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.CourseCard;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.CourseSubjectCard;
import com.elcazadordebaterias.coordinapp.utils.cards.groups.GroupCard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupsFragment_GroupalChat} factory method to
 * create an instance of this fragment.
 */
public class GroupsFragment_GroupalChat extends Fragment {

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    ArrayList<CourseCard> coursesList;
    CourseCardAdapter coursesAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        coursesList = new ArrayList<CourseCard>();
        coursesAdapter = new CourseCardAdapter(coursesList, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_groups_teacher_groupalchat, container, false);

        RecyclerView recyclerviewGroups = v.findViewById(R.id.recyclerviewGroups);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerviewGroups.setAdapter(coursesAdapter);
        recyclerviewGroups.setLayoutManager(layoutManager);

        fStore.collection("CoursesOrganization").whereArrayContains("allParticipantsIDs", fAuth.getUid()).get().addOnCompleteListener(getCourses -> {
            if (getCourses.isSuccessful()) {
                for (QueryDocumentSnapshot document : getCourses.getResult()) { // For each course the teacher is in

                    String courseName = document.getId();
                    ArrayList<CourseSubjectCard> subjectList = new ArrayList<CourseSubjectCard>();

                    CourseCard course = new CourseCard(courseName, subjectList);

                    fStore.collection("CoursesOrganization").document(courseName)
                            .collection("Subjects").whereEqualTo("teacherID", fAuth.getUid()).get().addOnCompleteListener(getSubjects -> {
                        if (getSubjects.isSuccessful()) {
                            for (QueryDocumentSnapshot subjectDocument : getSubjects.getResult()) {

                                String subjectName = subjectDocument.getId();
                                ArrayList<GroupCard> groupList = new ArrayList<GroupCard>();

                                fStore.collection("CoursesOrganization").document(courseName)
                                        .collection("Subjects").document(subjectName)
                                        .collection("Groups").whereArrayContains("participantsIds", fAuth.getUid()).get().addOnCompleteListener(getGroups -> {
                                    if (getGroups.isSuccessful()) {

                                        for (QueryDocumentSnapshot document11 : getGroups.getResult()) {

                                            Group group = document11.toObject(Group.class);
                                            ArrayList<String> participantsNames = new ArrayList<String>();

                                            for (GroupParticipant participant : group.getParticipants()) {
                                                participantsNames.add(participant.getParticipantFullName());
                                            }

                                            GroupCard groupCard = new GroupCard(document11.getId(), group.getGroupName(), group.getSubjectName(), participantsNames);
                                            groupList.add(groupCard);
                                        }
                                        if(groupList.size() >= 1) { // If there is no groups, we don't want them to show
                                            CourseSubjectCard subject = new CourseSubjectCard(subjectName, groupList);
                                            subjectList.add(subject);
                                            coursesList.add(course);
                                            coursesAdapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        return v;
    }

}