package com.elcazadordebaterias.coordinapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.CourseCardAdapter;
import com.elcazadordebaterias.coordinapp.adapters.PetitionGroupCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.CourseCard;
import com.elcazadordebaterias.coordinapp.utils.CourseParticipant;
import com.elcazadordebaterias.coordinapp.utils.CourseSubject;
import com.elcazadordebaterias.coordinapp.utils.GroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.PetitionGroupCard;
import com.elcazadordebaterias.coordinapp.utils.PetitionList;
import com.elcazadordebaterias.coordinapp.utils.PetitionRequest;
import com.elcazadordebaterias.coordinapp.utils.PetitionUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The fragment representing the Home Tab of the student.
 * @author Martín Mateos Sánchez
 */
public class HomeFragment_Student extends Fragment {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    CircularProgressIndicator loadingIndicator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_student, container, false);

        loadingIndicator = v.findViewById(R.id.loadingIndicator);
        loadingIndicator.setVisibility(View.VISIBLE);

        // Recyclerview 1 - Enrolled courses
        RecyclerView coursesRecyclerView = v.findViewById(R.id.recyclerview_courses);
        ArrayList<CourseCard> itemList = new ArrayList<CourseCard>();
        CourseCardAdapter courseCardAdapter = new CourseCardAdapter(itemList);
        LinearLayoutManager coursesLayoutManager = new LinearLayoutManager(getContext());

        coursesRecyclerView.setAdapter(courseCardAdapter);
        coursesRecyclerView.setLayoutManager(coursesLayoutManager);

        // Recyclerview 2 - Petitions
        RecyclerView groupsPetitionsRecyclerView = v.findViewById(R.id.recyclerview_petitions);
        LinearLayoutManager petitionsLayoutManager = new LinearLayoutManager(getContext());

        ArrayList<PetitionGroupCard> petitions = new ArrayList<PetitionGroupCard>();

        PetitionGroupCardAdapter petitionsAdapter = new PetitionGroupCardAdapter(petitions, getContext());
        groupsPetitionsRecyclerView.setAdapter(petitionsAdapter);
        groupsPetitionsRecyclerView.setLayoutManager(petitionsLayoutManager);
        

        // Create the list of the groups a student is in
        fStore.collection("CoursesOrganization").document("3ºESO B").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {

                    ArrayList<Map<String, Object>> data = (ArrayList<Map<String, Object>>) document.get("Subjects"); // Array of the subjects. Contains all the subjects from the current course
                    List<CourseSubject> courseSubjectList = new ArrayList<>();  // List with the information of the subjects

                    for (int i = 0; i < data.size(); i++) { // Iterate over all the subjects in the current course
                        Map<String, Object> subjectInfo = data.get(i); // Current subject information (the list with the students, the name of the subject and the teacher id)

                        ArrayList<String> studentsIds = (ArrayList<String>) subjectInfo.get("Students");
                        List<CourseParticipant> courseParticipantList = new ArrayList<>(); // List with the information of the students

                        if (studentsIds.contains(fAuth.getCurrentUser().getUid())) { // Check if the student is enrolled in the subject

                            fStore.collection("Students").get().addOnCompleteListener(task1 -> { // Search for student info to build the student list
                                if (task1.isSuccessful()) {
                                    // Add the teacher to be displayed
                                    fStore.collection("Teachers").document((String) subjectInfo.get("TeacherId")).get().addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful()) {

                                            DocumentSnapshot document2 = task2.getResult();

                                            courseParticipantList.add(new CourseParticipant("Profesor: " + document2.getData().get("FullName").toString(), document2.getData().get("UserEmail").toString()));

                                            for (QueryDocumentSnapshot document1 : task1.getResult()) { // Create the list of the students
                                                if (studentsIds.contains(document1.getId()) && !document1.getId().equals(fAuth.getCurrentUser().getUid())) { // The current user is not shown in the list
                                                    courseParticipantList.add(new CourseParticipant(document1.getData().get("FullName").toString(), document1.getData().get("UserEmail").toString()));
                                                }
                                            }

                                            courseSubjectList.add(new CourseSubject((String) subjectInfo.get("SubjectName"), courseParticipantList));
                                            courseCardAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            });

                        }
                    }
                    itemList.add(new CourseCard(document.getId(), courseSubjectList));
                    loadingIndicator.setVisibility(View.GONE);
                    coursesRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        // Create the list of the current unaccepted petitions
        fStore.collection("Petitions").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) { // Iterate over all students (each document) who have made a petition.
                    PetitionList data = document.toObject(PetitionList.class);

                    String requesterId = document.getId();

                    ArrayList<PetitionRequest> requestsList = data.getPetitionList();

                    for(PetitionRequest request : requestsList){ // Iterate over all the petitions the current student has made.
                        ArrayList<PetitionUser> participants = request.getPetitionUsersList();

                        ArrayList<String> participantsIds = new ArrayList<String>(); // All the IDs of the participants, including teachers and students.
                        for(PetitionUser user : participants){
                            participantsIds.add(user.getUserId());
                        }

                        if (participantsIds.contains(fAuth.getUid())){ // The request is for the current user. Make the petition card.
                            fStore.collection("Students").document(requesterId).get().addOnCompleteListener(getRequesterNameTask -> { // Get the name of the requester.
                                if (getRequesterNameTask.isSuccessful()){
                                    DocumentSnapshot document12 = getRequesterNameTask.getResult();
                                    if(document12.exists()){

                                        ArrayList<GroupParticipant> cardParticipants = new ArrayList<GroupParticipant>();
                                        String requesterName;
                                        requesterName = (String) document12.getData().get("FullName");

                                        for (int i = 0; i < participantsIds.size(); i++) { // Search for the name of the teacher, then the students.
                                            int position = i;
                                            fStore.collection("Teachers").document(participantsIds.get(i)).get().addOnCompleteListener(task12 -> { // Search for the name of the teacher
                                                if (task12.isSuccessful()) {
                                                    DocumentSnapshot document1 = task12.getResult();
                                                    if (document1.exists()) {
                                                        String teacherName = (String) document1.getData().get("FullName");
                                                        int petitionStatus = 0;

                                                        for (int j = 0; j < participants.size() ; j++) {// Get the status of the teacher petition.
                                                            PetitionUser currentUser = participants.get(j);
                                                            if(currentUser.getUserId().equals(participantsIds.get(position))){
                                                                petitionStatus = currentUser.getPetitionStatus();
                                                            }
                                                        }

                                                        int petitionStatusImage;

                                                        if(petitionStatus == 0){
                                                            petitionStatusImage = R.drawable.petition_pending;
                                                        }else if(petitionStatus == 1){
                                                            petitionStatusImage = R.drawable.petition_accepted;
                                                        }else{
                                                            petitionStatusImage = R.drawable.petition_rejected;
                                                        }

                                                        cardParticipants.add(new GroupParticipant(teacherName, petitionStatusImage));
                                                        petitionsAdapter.notifyDataSetChanged();

                                                        // Search for the names of the students

                                                    }

                                                }
                                            });

                                        }
                                        petitions.add(new PetitionGroupCard(requesterName,request.getCourse() + " / " + request.getSubject(), cardParticipants));
                                    }
                                }
                            });
                        }
                    }

                }
            }
        });

        return v;
    }

}