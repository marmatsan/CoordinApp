package com.elcazadordebaterias.coordinapp.utils.dialogs.studentdialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.listviews.SelectParticipantsListAdapter;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.SelectParticipantItem;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.CollectiveGroupDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionRequest;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionUser;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class RequestGroupDialog extends DialogFragment {

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    private Context context;

    private ListView participantsListView;


    private ArrayList<SelectParticipantItem> participantsList;
    private SelectParticipantsListAdapter participantsAdapter;

    private final String selectedCourse;
    private final String selectedSubject;

    public RequestGroupDialog(String selectedCourse, String selectedSubject) {
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
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.utils_dialogs_requestgroupdialog, null);

        // List of participants
        participantsListView = view.findViewById(R.id.participantsListView);
        participantsListView.setAdapter(participantsAdapter);

        populateParticipants();

        builder.setView(view).setTitle("Petición para crear un grupo")
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    // Just closes the dialog
                })
                .setPositiveButton("Crear", (dialogInterface, i) -> {

                    ArrayList<PetitionUser> petitionUsersList = new ArrayList<PetitionUser>();
                    ArrayList<String> petitionUsersIds = new ArrayList<String>();

                    for (SelectParticipantItem item : participantsList) {
                        if (item.isSelected()) {
                            String participantId = item.getParticipantId();
                            petitionUsersList.add(new PetitionUser(participantId, item.getParticipantName(), PetitionUser.STATUS_PENDING));
                            petitionUsersIds.add(participantId);
                        }
                    }

                    if (petitionUsersList.size() == 0) {
                        Toast.makeText(context, "Debes agregar al menos a un miembro más al grupo", Toast.LENGTH_SHORT).show();
                    } else {

                        fStore
                                .collection("Students")
                                .document(fAuth.getUid())
                                .get()
                                .addOnSuccessListener(requesterDocument -> {
                                    String requesterID = requesterDocument.getId();
                                    String requesterName = (String) requesterDocument.get("FullName");

                                    petitionUsersList.add(new PetitionUser(
                                            requesterID,
                                            requesterName,
                                            PetitionUser.STATUS_ACCEPTED)
                                    );
                                    petitionUsersIds.add(requesterID);

                                    DocumentReference subjectDocRef = fStore
                                            .collection("CoursesOrganization")
                                            .document(selectedCourse)
                                            .collection("Subjects")
                                            .document(selectedSubject);

                                    CollectionReference petitionsCollRef = subjectDocRef
                                            .collection("Petitions");

                                    subjectDocRef // Check if the group that we are trying to make already exists
                                            .get()
                                            .addOnSuccessListener(documentSnapshot -> {
                                                Subject subject = documentSnapshot.toObject(Subject.class);
                                                String teacherID = subject.getTeacherID();

                                                ArrayList<String> allParticipantsIDs = new ArrayList<String>(petitionUsersIds);
                                                allParticipantsIDs.add(teacherID);

                                                subjectDocRef
                                                        .collection("CollectiveGroups")
                                                        .get()
                                                        .addOnSuccessListener(groupDocuments -> {

                                                            boolean groupExists = false;

                                                            for (DocumentSnapshot groupDoc : groupDocuments) {
                                                                CollectiveGroupDocument collectiveGroupDocument = groupDoc.toObject(CollectiveGroupDocument.class);
                                                                if (collectiveGroupDocument.getAllParticipantsIDs().containsAll(allParticipantsIDs)) {
                                                                    groupExists = true;
                                                                    break;
                                                                }
                                                            }

                                                            if (groupExists) {
                                                                Toast.makeText(context, "Ya estás en un grupo igual para el que estás intentando solicitar crear con esta petición", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                petitionsCollRef
                                                                        .get()
                                                                        .addOnSuccessListener(petitionDocuments -> {

                                                                            int numOfPetitions = 0;
                                                                            boolean petitionExists = false;

                                                                            for (DocumentSnapshot petitionDoc : petitionDocuments) {
                                                                                PetitionRequest petitionRequest = petitionDoc.toObject(PetitionRequest.class);
                                                                                if (petitionRequest.getPetitionUsersIds().containsAll(petitionUsersIds)) {
                                                                                    petitionExists = true;
                                                                                    break;
                                                                                } else if (petitionRequest.getRequesterId().equals(fAuth.getUid())) {
                                                                                    numOfPetitions = numOfPetitions + 1;
                                                                                }
                                                                            }
                                                                            if (petitionExists) {
                                                                                Toast.makeText(context, "Ya has creado una petición igual a esta", Toast.LENGTH_SHORT).show();
                                                                            } else {
                                                                                if (numOfPetitions >= 3) {
                                                                                    Toast.makeText(context, "No puedes hacer más de tres peticiones de creación de grupo. " +
                                                                                            "Espera a que el tutor acepte o rechace las peticiones que ya tienes", Toast.LENGTH_LONG).show();
                                                                                } else {

                                                                                    PetitionRequest newPetition = new PetitionRequest(
                                                                                            requesterID,
                                                                                            requesterName,
                                                                                            teacherID,
                                                                                            petitionUsersIds,
                                                                                            petitionUsersList
                                                                                    );

                                                                                    petitionsCollRef.add(newPetition);

                                                                                }
                                                                            }
                                                                        });
                                                            }

                                                        });

                                            });

                                });

                    }
                });

        return builder.create();
    }

    private void populateParticipants() {
        fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Subject subject = documentSnapshot.toObject(Subject.class);
                    ArrayList<String> studentsIDs = subject.getStudentIDs();
                    studentsIDs.remove(fAuth.getUid());

                    fStore
                            .collection("Students")
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                Log.d("DEBUGGING", ""+queryDocumentSnapshots.size());

                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    Log.d("DEBUGGING", (String) document.get("FullName"));

                                    if (studentsIDs.contains(document.getId())) {
                                        Log.d("DEBUGGING", "IN");
                                        participantsList.add(new SelectParticipantItem((String) document.get("FullName"), document.getId()));
                                    }
                                }
                                participantsAdapter.notifyDataSetChanged();
                            });

                });
    }

}
