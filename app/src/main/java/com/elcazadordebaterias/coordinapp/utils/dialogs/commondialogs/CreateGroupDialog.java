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
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupDocument;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.GroupParticipant;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionRequest;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.PetitionUser;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.SelectParticipantItem;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
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
        } else if (userType == UserType.TYPE_TEACHER) {
            dialogTitle = "Crear un único grupo";
        }

        builder.setView(view).setTitle(dialogTitle)
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    // Just closes the dialog
                })
                .setPositiveButton("Crear", (dialogInterface, i) -> {

                    if (userType == UserType.TYPE_STUDENT) { // Create the petition to form a group

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
                                                                    GroupDocument groupDocument = groupDoc.toObject(GroupDocument.class);
                                                                    if (groupDocument.getAllParticipantsIDs().equals(allParticipantsIDs)) {
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
                                                                                    if (petitionRequest.getRequesterId().equals(fAuth.getUid())) {
                                                                                        numOfPetitions = numOfPetitions + 1;
                                                                                    } else if (petitionRequest.getPetitionUsersIds().equals(petitionUsersIds)) {
                                                                                        petitionExists = true;
                                                                                        break;
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

                    } else if (userType == UserType.TYPE_TEACHER) { // Directly create the group

                        ArrayList<String> studentsIDs = new ArrayList<String>();

                        // Add all selected students to the petition.
                        for (SelectParticipantItem item : participantsList) {
                            if (item.isSelected()) {
                                studentsIDs.add(item.getParticipantId());
                            }
                        }

                        if (studentsIDs.size() < 1) {
                            Toast.makeText(context, "Debes agregar al menos a un miembro más al grupo", Toast.LENGTH_SHORT).show();
                        } else {

                            DocumentReference subjectRef = fStore
                                    .collection("CoursesOrganization")
                                    .document(selectedCourse)
                                    .collection("Subjects")
                                    .document(selectedSubject);

                            CollectionReference collectionRef;

                            if (studentsIDs.size() == 1) {
                                collectionRef = subjectRef.collection("IndividualGroups");
                            } else {
                                collectionRef = subjectRef.collection("CollectiveGroups");
                            }

                            collectionRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
                                int maxIdentifier = Group.getMaxGroupIdentifier(queryDocumentSnapshots);
                                Group.createGroup(collectionRef, selectedCourse, selectedSubject, studentsIDs, maxIdentifier + 1, context, userType);
                            });

                        }
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
                            .whereIn(FieldPath.documentId(), studentsIDs)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    participantsList.add(new SelectParticipantItem((String) document.get("FullName"), document.getId()));
                                }
                                participantsAdapter.notifyDataSetChanged();
                            });

                });
    }

}
