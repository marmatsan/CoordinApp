package com.coordinapprest.coordinapprest.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.annotation.PostConstruct;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.springframework.stereotype.Service;

@Service
public class FirebaseInitialize {
    
    @PostConstruct
    public void initialize() throws IOException {
        FileInputStream serviceAccount;
        
        String path = System.getProperty("user.dir");
        String keyPath = path + "/coordinapprest/serviceAccountKey.json";

        try {
            serviceAccount = new FileInputStream(keyPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://coordinapp-86f14.firebaseio.com/")
            .build();
        
        FirebaseApp.initializeApp(options);
    }

}
