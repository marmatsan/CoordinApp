package com.elcazadordebaterias.coordinapp.utils.dialogs.commondialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.listviews.SelectParticipantsListAdapter;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.UserType;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionRequest;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionUser;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.SelectParticipantItem;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * @author Martín Mateos Sánchez
 */
public class CreateGroupDialog extends DialogFragment {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private Context context;

    private Spinner courseListSpinner, subjectListSpinner;
    private ListView participantsListView;

    private final int userType;

    private ArrayList<String> coursesNames;
    private ArrayList<String> subjectsNames;
    private ArrayList<SelectParticipantItem> participantsList;

    private ArrayAdapter<String> coursesAdapter;
    private ArrayAdapter<String> subjectsAdapter;
    private SelectParticipantsListAdapter participantsAdapter;

    private String selectedCourse;
    private String selectedSubject;

    public CreateGroupDialog(int userType){
        this.userType = userType;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        coursesNames = new ArrayList<String>();
        subjectsNames = new ArrayList<String>();
        participantsList = new ArrayList<SelectParticipantItem>();

        coursesAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, coursesNames);
        subjectsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, subjectsNames);
        participantsAdapter = new SelectParticipantsListAdapter(getContext(), participantsList);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) { // TODO: Totally improve this class for errors
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.utils_creategroupdialog, null);

        // Spinners
        courseListSpinner = view.findViewById(R.id.courseNameSpinner);
        subjectListSpinner = view.findViewById(R.id.subjectNameSpinner);

        // List of participants
        participantsListView = view.findViewById(R.id.participantsList);

        // Spinners configuration
        coursesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        courseListSpinner.setAdapter(coursesAdapter);
        courseListSpinner.setSelection(0);

        subjectListSpinner.setAdapter(subjectsAdapter);
        subjectListSpinner.setSelection(0);

        // ListView configuration
        participantsListView.setAdapter(participantsAdapter);

        // Get the courses list
        populateCoursesName();

       courseListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // When we change the course, we update the subjects, and the update of the subjects leads to the update of the participants list
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               selectedCourse = parent.getItemAtPosition(position).toString();
               updateSubjectsList(userType, selectedCourse);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

       subjectListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){ // When we change the subject, we only have to update the participants list
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               selectedSubject = parent.getItemAtPosition(position).toString();
               updateParticipantsList(selectedCourse, selectedSubject);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

        builder.setView(view).setTitle("Crear un único grupo")
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    // Just closes the dialog
                })
                .setPositiveButton("Crear", (dialogInterface, i) -> {

                    String selectedCourse = courseListSpinner.getSelectedItem().toString();
                    String selectedSubject = subjectListSpinner.getSelectedItem().toString();

                    if (userType == UserType.TYPE_STUDENT){

                        ArrayList<PetitionUser> petitionUsersList = new ArrayList<PetitionUser>();
                        ArrayList<String> petitionUsersIds = new ArrayList<String>();

                        String requesterId = fAuth.getUid();

                        for (SelectParticipantItem item : participantsList) {
                            if (item.isSelected()) {
                                String participantId = item.getParticipantId();
                                petitionUsersList.add(new PetitionUser(participantId, item.getParticipantName(), PetitionUser.STATUS_PENDING));
                                petitionUsersIds.add(participantId);
                            }
                        }

                        if (petitionUsersList.size() <= 1){
                            Toast.makeText(context, "Debes agregar al menos a un miembro más al grupo", Toast.LENGTH_SHORT).show();
                        } else {

                            fStore.collection("Petitions").whereEqualTo("requesterId", fAuth.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {

                                for(QueryDocumentSnapshot petitionDoc : queryDocumentSnapshots){
                                    PetitionRequest petition = petitionDoc.toObject(PetitionRequest.class);
                                    if(petition.getCourse().equals(selectedCourse)
                                            && petition.getSubject().equals(selectedSubject)
                                            && petition.getRequesterId().equals(fAuth.getUid())) {
                                        Toast.makeText(context, "Ya has hecho una petición de esta asignatura. " +
                                                "Espera a que el tutor la acepte o la rechace", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }

                                fStore.collection("CoursesOrganization").document(selectedCourse)
                                        .collection("Subjects").document(selectedSubject)
                                        .get().addOnSuccessListener(subjectDocument -> {
                                    Subject subject = subjectDocument.toObject(Subject.class);
                                    String teacherID = subject.getTeacherID();

                                    fStore.collection("Teachers").document(teacherID).get().addOnSuccessListener(teacherDocument -> {
                                        String teacherName = (String) teacherDocument.get("FullName");

                                        fStore.collection("Students").document(requesterId)
                                                .get().addOnSuccessListener(requesterDocument -> {

                                            petitionUsersList.add(new PetitionUser(requesterId,
                                                    (String) requesterDocument.getData().get("FullName"),
                                                    PetitionUser.STATUS_ACCEPTED));

                                            petitionUsersIds.add(requesterId);

                                            PetitionRequest petition = new PetitionRequest(selectedCourse,
                                                    selectedSubject,
                                                    requesterId,
                                                    (String) requesterDocument.getData().get("FullName"),
                                                    teacherID,
                                                    teacherName,
                                                    petitionUsersIds,
                                                    petitionUsersList);

                                            fStore.collection("Petitions").add(petition);
                                        });
                                    });
                                });
                            });
                        }

                    } else if (userType == UserType.TYPE_TEACHER){ // Directly create the group

                        ArrayList<String> studentsIDs = new ArrayList<String>();
                        ArrayList<GroupParticipant> participants = new ArrayList<GroupParticipant>();

                        // Add all selected students to the petition.
                        for (SelectParticipantItem item : participantsList) {
                            if (item.isSelected()) {
                                participants.add(new GroupParticipant(item.getParticipantName(), item.getParticipantId()));
                                studentsIDs.add(item.getParticipantId());
                            }
                        }

                        if(participants.size() <= 1){
                            Toast.makeText(context, "Debes agregar al menos a un miembro más al grupo", Toast.LENGTH_SHORT).show();
                        } else {
                            fStore.collection("Teachers").document(fAuth.getUid()).get().addOnSuccessListener(teacherDocument -> {

                                Group group = new Group(fAuth.getUid(),
                                        (String) teacherDocument.get("FullName"),
                                        selectedCourse,
                                        selectedSubject,
                                        studentsIDs,
                                        participants);

                                fStore.collection("CoursesOrganization")
                                        .document(group.getCourseName())
                                        .collection("Subjects")
                                        .document(group.getSubjectName())
                                        .collection("Groups").add(group).addOnSuccessListener(documentReference -> { // Write groups info of students and teacher
                                            fStore.collection("Students").whereIn(FieldPath.documentId(), studentsIDs);
                                        });
                            });
                        }

                    }
                });

        return builder.create();
    }

    private void updateSubjectsList(int userType, String selectedCourse){
        ArrayList<String> updateSubjects = new ArrayList<String>();

        if(userType == UserType.TYPE_STUDENT){ // The user is a student
            fStore.collection("CoursesOrganization").document(selectedCourse)
                    .collection("Subjects").whereArrayContains("studentIDs", fAuth.getUid()).get()
                    .addOnCompleteListener(task -> { // Get the name of the subjects
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        updateSubjects.add(document.getId());
                    }

                    subjectsNames.clear();
                    subjectsNames.addAll(updateSubjects);

                    selectedSubject = subjectsNames.get(0); // TODO: If there are no subjects, updateParticipantsList will produce a nullpointerexception

                    updateParticipantsList(selectedCourse, selectedSubject);

                    subjectsAdapter.notifyDataSetChanged();
                }
            });
        } else if (userType == UserType.TYPE_TEACHER) { // The user is a teacher
            fStore.collection("CoursesOrganization").document(selectedCourse).collection("Subjects").whereEqualTo("teacherID", fAuth.getUid()).get().addOnCompleteListener(task -> { // Get the name of the subjects
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        updateSubjects.add(document.getId());
                    }

                    subjectsNames.clear();
                    subjectsNames.addAll(updateSubjects);

                    selectedSubject = subjectsNames.get(0); // TODO: If there are no subjects, updateParticipantsList will produce a nullpointerexception

                    updateParticipantsList(selectedCourse, selectedSubject);

                    subjectsAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    private void updateParticipantsList(String selectedCourse, String selectedSubject) {
        ArrayList<SelectParticipantItem> updateParticipants = new ArrayList<SelectParticipantItem>();

        fStore.collection("CoursesOrganization").document(selectedCourse)
                .collection("Subjects").document(selectedSubject).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Subject subject = documentSnapshot.toObject(Subject.class);

                    fStore.collection("Students").whereIn(FieldPath.documentId(), subject.getStudentIDs()).get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if(!document.getId().equals(fAuth.getUid())) { // We don't want the current user to be shown in the list. It is included by default
                                updateParticipants.add(new SelectParticipantItem((String) document.get("FullName"), document.getId(), false));
                            }
                        }
                        participantsList.clear();
                        participantsList.addAll(updateParticipants);

                        participantsAdapter.notifyDataSetChanged();
                    });
                });
    }

    private void populateCoursesName(){
        fStore.collection("CoursesOrganization").whereArrayContains("allParticipantsIDs", fAuth.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                coursesNames.add(document.getId());
            }
            coursesAdapter.notifyDataSetChanged();
        });
    }

}