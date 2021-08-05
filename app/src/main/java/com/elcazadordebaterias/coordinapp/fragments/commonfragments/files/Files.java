package com.elcazadordebaterias.coordinapp.fragments.commonfragments.files;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.CourseParticipantAdapter;
import com.elcazadordebaterias.coordinapp.adapters.recyclerviews.files.FilesContainerCardAdapter;
import com.elcazadordebaterias.coordinapp.utils.cards.CourseParticipantCard;
import com.elcazadordebaterias.coordinapp.utils.cards.files.FileCard;
import com.elcazadordebaterias.coordinapp.utils.cards.files.FilesContainerCard;
import com.elcazadordebaterias.coordinapp.utils.firesoredatamodels.StorageFileReference;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Files extends Fragment {

    // Firestore
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    // Type of user who called this fragment
    private final int userType;

    private String selectedCourse;
    private String selectedSubject;

    public Files(int userType, String selectedCourse, String selectedSubject) {
        this.userType = userType;
        this.selectedCourse = selectedCourse;
        this.selectedSubject = selectedSubject;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commonfragments_files, container, false);

        TextView noFiles = view.findViewById(R.id.noFiles);
        RecyclerView filesContainer = view.findViewById(R.id.filesContainer);

        ArrayList<FilesContainerCard> groupsList = new ArrayList<FilesContainerCard>();
        FilesContainerCardAdapter filesContainersAdapter = new FilesContainerCardAdapter(groupsList, getContext());

        LinearLayoutManager coursesLayoutManager = new LinearLayoutManager(getContext());

        filesContainer.setAdapter(filesContainersAdapter);
        filesContainer.setLayoutManager(coursesLayoutManager);

        CollectionReference collectionRef = fStore
                .collection("CoursesOrganization")
                .document(selectedCourse)
                .collection("Subjects")
                .document(selectedSubject)
                .collection("CollectiveGroups");

        Query getGroups = collectionRef.whereArrayContains("participantsIds", fAuth.getUid());

        getGroups.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot document : queryDocumentSnapshots) {
                String groupName = (String) document.get("name");
                ArrayList<FileCard> filesList = new ArrayList<FileCard>();

                collectionRef
                        .document(document.getId())
                        .collection("Storage")
                        .get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                    for (DocumentSnapshot document1 : queryDocumentSnapshots1){
                              StorageFileReference storageRef = document1.toObject(StorageFileReference.class);

                              filesList.add(new FileCard(
                                      R.drawable.ic_baseline_insert_drive_file_24,
                                      storageRef.getFileName(),
                                      storageRef.getUploaderName(),
                                      storageRef.getUploadedDate(),
                                      storageRef.getDownloadLink()
                              ));
                            Log.d("DEBUGGING", "Got file");
                          }
                    groupsList.add(new FilesContainerCard(groupName, filesList));
                });
            }
            filesContainersAdapter.notifyDataSetChanged();
        });

        return view;
    }

}
