package com.elcazadordebaterias.coordinapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.EmailValidation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText userEmail, userPassword;
    MaterialButton login;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        userEmail = findViewById(R.id.login_user_email_text);
        userPassword = findViewById(R.id.login_user_password_text);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fieldsOk()){
                    fAuth.signInWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString()).addOnSuccessListener(authResult -> {

                    }).addOnFailureListener(e -> {

                    });
                }else{

                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    private boolean fieldsOk(){

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if(email.isEmpty() || !EmailValidation.isValidEmail(email)){
            return false;
        }else return !password.isEmpty();

    }



    public void goToRegister(View view){
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }

}
