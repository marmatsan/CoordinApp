package com.coordinapprest.coordinapprest.service;

import java.util.concurrent.ExecutionException;

import com.coordinapprest.coordinapprest.models.Course;
import com.coordinapprest.coordinapprest.models.Subject;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.stereotype.Service;

@Service
public class FirebaseService {

    public String postCourse(Course course) throws InterruptedException, ExecutionException{
        Firestore fStore = FirestoreClient.getFirestore();
        ApiFuture<WriteResult> future = null;

        fStore.collection("CoursesOrganization").document(course.getCourseName()).set(course.getCourseParticipantsIDs());

        for(Subject subject : course.getCourseSubjects()){
            future = fStore.collection("CoursesOrganization").document(course.getCourseName()).collection("Subjects").document(subject.getSubjectName()).set(subject);
        }
       
        return future.get().getUpdateTime().toString();
    }
    
}
