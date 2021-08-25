package com.elcazadordebaterias.coordinapp.utils.dialogs.teacherdialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.listviews.SelectParticipantsWithSpokerAdapter;
import com.elcazadordebaterias.coordinapp.utils.customdatamodels.SelectParticipantItemWithSpoker;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.restmodel.Subject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CreateGroupDialog extends DialogFragment {

    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;

    private Context context;

    private ListView participantsListView;

    private ArrayList<SelectParticipantItemWithSpoker> participantsList;
    private SelectParticipantsWithSpokerAdapter participantsAdapter;

    private final String selectedCourse;
    private final String selectedSubject;

    public CreateGroupDialog(String selectedCourse, String selectedSubject) {
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        participantsList = new ArrayList<SelectParticipantItemWithSpoker>();
        participantsAdapter = new SelectParticipantsWithSpokerAdapter(getContext(), participantsList);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.utils_dialogs_creategroupdialog, null);

        // List of participants
        participantsListView = view.findViewById(R.id.participantsList);
        participantsListView.setAdapter(participantsAdapter);

        populateParticipants();

        builder.setView(view).setTitle("Crear un único grupo")
                .setNegativeButton("Cancelar", (dialogInterface, i) -> {
                    // Just closes the dialog
                })
                .setPositiveButton("Crear", (dialogInterface, i) -> {

                    ArrayList<String> studentsIDs = new ArrayList<String>();

                    // Add all selected students to the petition.
                    for (SelectParticipantItemWithSpoker item : participantsList) {
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
                            Group.createGroup(collectionRef, selectedCourse, selectedSubject, studentsIDs, maxIdentifier + 1, context, null, null);
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
                            .whereIn(FieldPath.documentId(), studentsIDs)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    participantsList.add(new SelectParticipantItemWithSpoker((String) document.get("FullName"), document.getId()));
                                }
                                participantsAdapter.notifyDataSetChanged();
                            });

                });
    }

}
