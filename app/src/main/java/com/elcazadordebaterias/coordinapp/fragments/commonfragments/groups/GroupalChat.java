package com.elcazadordebaterias.coordinapp.fragments.commonfragments.groups;

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
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupalChat} factory method to
 * create an instance of this fragment.
 */
public class GroupalChat extends Fragment {

    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    private ArrayList<CourseCard> coursesList;
    private ArrayList<GroupCard> groupsList;
    private CourseCardAdapter coursesAdapter;

    private int userType;

    public GroupalChat(int userType) {
        this.userType = userType;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        coursesList = new ArrayList<CourseCard>();
        groupsList = new ArrayList<GroupCard>();

        coursesAdapter = new CourseCardAdapter(coursesList, getContext(), userType);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_groups_groupalchat, container, false);

        RecyclerView recyclerviewGroups = view.findViewById(R.id.recyclerviewGroups);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerviewGroups.setAdapter(coursesAdapter);
        recyclerviewGroups.setLayoutManager(layoutManager);

        if (userType == UserType.TYPE_STUDENT) {
            fStore.collectionGroup("Groups").whereArrayContains("participantsIds", fAuth.getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    coursesListSetup(queryDocumentSnapshots);
                }
            });
        } else if (userType == UserType.TYPE_TEACHER) {
            fStore.collectionGroup("Groups").whereEqualTo("coordinatorId", fAuth.getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    coursesListSetup(queryDocumentSnapshots);
                }
            });
        }
        return view;
    }

    private void populateGroups(QuerySnapshot queryDocumentSnapshots) {
        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
            Group group = document.toObject(Group.class);
            ArrayList<String> participantNames = new ArrayList<String>();

            for (int i = 0; i < group.getParticipants().size(); i++) {
                participantNames.add(group.getParticipants().get(i).getParticipantFullName());
            }

            groupsList.add(new GroupCard(group.getGroupName(),document.getId(), group.getCourseName(), group.getSubjectName(), participantNames));
        }
    }

    /**
     * Returns a map whose key is the name of the course and whose value is an arraylist of groupcards
     * belonging to the same course.
     *
     * @return
     */
    private Map<String, ArrayList<GroupCard>> shortByCourseName() {
        Map<String, ArrayList<GroupCard>> map = new HashMap<String, ArrayList<GroupCard>>();

        for (GroupCard groupCard : groupsList) {
            ArrayList<GroupCard> list = map.get(groupCard.getCourseName());
            if (list == null) {
                list = new ArrayList<GroupCard>();
                map.put(groupCard.getCourseName(), list);
            }
            list.add(groupCard);
        }

        return map;
    }

    /**
     * Returns a map whose key is the name of the course and whose value is another map whose key is the name
     * of the subject and whose value is an arraylist of groupcards belonging to the same subject.
     *
     * @param data
     * @return
     */
    private Map<String, Map<String, ArrayList<GroupCard>>> shortBySubjectName(Map<String, ArrayList<GroupCard>> data) {
        Map<String, Map<String, ArrayList<GroupCard>>> map = new HashMap<String, Map<String, ArrayList<GroupCard>>>();

        for (Map.Entry<String, ArrayList<GroupCard>> entry : data.entrySet()) {
            Map<String, ArrayList<GroupCard>> subjectsMap = new HashMap<String, ArrayList<GroupCard>>();

            for (GroupCard group : entry.getValue()) {
                ArrayList<GroupCard> list = subjectsMap.get(group.getSubjectName());
                if (list == null) {
                    list = new ArrayList<GroupCard>();
                    subjectsMap.put(group.getSubjectName(), list);
                }
                list.add(group);
            }
            map.put(entry.getKey(), subjectsMap);
        }

        return map;
    }

    private void populateCourses(Map<String, Map<String, ArrayList<GroupCard>>> data) {
        for (Map.Entry<String, Map<String, ArrayList<GroupCard>>> courseEntry : data.entrySet()) {

            ArrayList<CourseSubjectCard> subjectsList = new ArrayList<CourseSubjectCard>();
            CourseCard courseCard = new CourseCard(courseEntry.getKey(), subjectsList);

            for(Map.Entry<String, ArrayList<GroupCard>> subjectEntry : courseEntry.getValue().entrySet()){
                subjectsList.add(new CourseSubjectCard(subjectEntry.getKey(), subjectEntry.getValue()));
            }

            coursesList.add(courseCard);
        }
        coursesAdapter.notifyDataSetChanged();
    }

    private void coursesListSetup(QuerySnapshot queryDocumentSnapshots){
        populateGroups(queryDocumentSnapshots);
        Map<String, ArrayList<GroupCard>> shortedByCourseName = shortByCourseName();
        Map<String, Map<String, ArrayList<GroupCard>>> shortedBySubjectName = shortBySubjectName(shortedByCourseName);
        populateCourses(shortedBySubjectName);
    }

}