package com.elcazadordebaterias.coordinapp.fragments.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.files.student.GroupContainerCardListAdapter;

import com.elcazadordebaterias.coordinapp.utils.cards.files.FileCard;
import com.elcazadordebaterias.coordinapp.utils.cards.files.FilesContainerCard;
import com.elcazadordebaterias.coordinapp.utils.cards.files.student.GroupContainerCard;

import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.Group;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.CollectiveGroupDocument;

import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.StorageFile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class StudentFiles extends Fragment {

    // Firestore
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    private String selectedCourse;
    private String selectedSubject;

    ArrayList<GroupContainerCard> groupsList;
    GroupContainerCardListAdapter groupsContainerAdapter;

    TextView noFiles;

    public StudentFiles(String selectedCourse, String selectedSubject) {
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        groupsList = new ArrayList<GroupContainerCard>();
        groupsContainerAdapter = new GroupContainerCardListAdapter(groupsList, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_files_groupalfiles, container, false);

        noFiles = view.findViewById(R.id.noFiles);
        RecyclerView filesContainer = view.findViewById(R.id.groupalFilesContainer);

        LinearLayoutManager coursesLayoutManager = new LinearLayoutManager(getContext());

        filesContainer.setAdapter(groupsContainerAdapter);
        filesContainer.setLayoutManager(coursesLayoutManager);

        CollectionReference collectionRef = fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("CollectiveGroups");

        collectionRef
                .whereArrayContains("allParticipantsIDs", fAuth.getUid())
                .addSnapshotListener((queryDocumentsSnapshots, error) -> {

                    if (error != null) {
                        return;
                    } else if (queryDocumentsSnapshots == null) {
                        return;
                    }

                    groupsList.clear();
                    for (DocumentSnapshot groupDocument : queryDocumentsSnapshots) {
                        ArrayList<FilesContainerCard> filesContainerList = new ArrayList<FilesContainerCard>();
                        CollectiveGroupDocument groupDoc = groupDocument.toObject(CollectiveGroupDocument.class);
                        String groupName = groupDoc.getName();

                        for (Group group : groupDoc.getGroups()) {
                            ArrayList<FileCard> filesList = new ArrayList<FileCard>();
                            String name = group.getName();
                            String storageCollection;

                            if (group.getHasTeacher()) {
                                storageCollection = "StorageWithTeacher";
                            } else {
                                storageCollection = "StorageWithoutTeacher";
                            }

                            collectionRef
                                    .document(groupDocument.getId())
                                    .collection(storageCollection)
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        for (DocumentSnapshot storageFileDoc : queryDocumentSnapshots) {
                                            StorageFile storageFile = storageFileDoc.toObject(StorageFile.class);
                                            FileCard fileCard = new FileCard(
                                                    R.drawable.ic_baseline_insert_drive_file_24,
                                                    storageFile.getUploaderName(),
                                                    storageFile.getFileName(),
                                                    storageFile.getUploadedDate(),
                                                    storageFile.getDownloadLink());
                                            filesList.add(fileCard);
                                        }
                                        listChanged();
                                    });
                            filesContainerList.add(new FilesContainerCard(name, filesList));
                        }
                        groupsList.add(new GroupContainerCard(groupName, filesContainerList));
                    }
                });

        return view;
    }

    private void listChanged() {
        groupsContainerAdapter.notifyDataSetChanged();

        if (groupsList.isEmpty()) {
            noFiles.setVisibility(View.VISIBLE);
        } else {
            noFiles.setVisibility(View.GONE);
        }
    }

}
