package com.elcazadordebaterias.coordinapp.utils.dialogs.commondialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * @author Martín Mateos Sánchez
 */
public class CreateGroupDialog extends DialogFragment {

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    private Context context;

    private ListView participantsListView;

    private final int userType;

    private ArrayList<SelectParticipantItem> participantsList;
    private SelectParticipantsListAdapter participantsAdapter;

    private final String selectedCourse;
    private final String selectedSubject;

    public CreateGroupDialog(int userType, String selectedCourse, String selectedSubject) {
        this.userType = userType;
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        participantsList = new ArrayList<SelectParticipantItem>();
        participantsAdapter = new SelectParticipantsListAdapter(getContext(), participantsList);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) { // TODO: Totally improve this class for errors
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.utils_creategroupdialog, null);

        // List of participants
        participantsListView = view.findViewById(R.id.participantsList);
        participantsListView.setAdapter(participantsAdapter);

        populateParticipants();

        String dialogTitle = null;
        
        if (userType == UserType.TYPE_STUDENT) {
            dialogTitle = "Petición para crear un grupo";
        } else if (userType == UserType.TYPE_TEACHER){
            dialogTitle = "Crear un único grupo";
        }
        
        builder.setView(view).setTitle(dialogTitle)
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    // Just closes the dialog
                })
                .setPositiveButton("Crear", (dialogInterface, i) -> {

                    if (userType == UserType.TYPE_STUDENT) {

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

                        if (petitionUsersList.size() <= 1) {
                            Toast.makeText(context, "Debes agregar al menos a un miembro más al grupo", Toast.LENGTH_SHORT).show();
                        } else {

                            fStore.collection("Petitions").whereEqualTo("requesterId", fAuth.getUid()).get().addOnSuccessListener(queryDocumentSnapshots -> {

                                for (QueryDocumentSnapshot petitionDoc : queryDocumentSnapshots) {
                                    PetitionRequest petition = petitionDoc.toObject(PetitionRequest.class);
                                    if (petition.getCourse().equals(selectedCourse)
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

                    } else if (userType == UserType.TYPE_TEACHER) { // Directly create the group

                        ArrayList<String> studentsIDs = new ArrayList<String>();
                        ArrayList<GroupParticipant> participants = new ArrayList<GroupParticipant>();

                        // Add all selected students to the petition.
                        for (SelectParticipantItem item : participantsList) {
                            if (item.isSelected()) {
                                participants.add(new GroupParticipant(item.getParticipantName(), item.getParticipantId()));
                                studentsIDs.add(item.getParticipantId());
                            }
                        }

                        if (participants.size() <= 1) {
                            Toast.makeText(context, "Debes agregar al menos a un miembro más al grupo", Toast.LENGTH_SHORT).show();
                        } else {
                            fStore.collection("Teachers").document(fAuth.getUid()).get().addOnSuccessListener(teacherDocument -> {

                                Group group = new Group(
                                        fAuth.getUid(),
                                        (String) teacherDocument.get("FullName"),
                                        selectedCourse,
                                        selectedSubject,
                                        studentsIDs,
                                        participants);

                                    group.createAndCommit(fStore, context);

                            });
                        }
                    }
                });

        return builder.create();
    }

    private void populateParticipants(){
        fStore
                .collection("CoursesOrganization").document(selectedCourse)
                .collection("Subjects").document(selectedSubject).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Subject subject = documentSnapshot.toObject(Subject.class);
                    ArrayList<String> studentsIDs = subject.getStudentIDs();

                    fStore.collection("Students").whereIn(FieldPath.documentId(), studentsIDs)
                            .get().addOnSuccessListener(queryDocumentSnapshots -> {
                                for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                                    participantsList.add(new SelectParticipantItem((String) document.get("FullName"), document.getId()));
                                }
                                participantsAdapter.notifyDataSetChanged();
                            });

                });
    }

}
