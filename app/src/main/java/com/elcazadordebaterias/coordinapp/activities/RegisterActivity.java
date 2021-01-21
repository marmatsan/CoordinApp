package com.elcazadordebaterias.coordinapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.EmailValidation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText userFullname, userEmail, userPassword, userRepPassword;
    MaterialButton register;
    SwitchMaterial userIsAdmin;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userFullname = findViewById(R.id.register_user_fullname_text);
        userEmail = findViewById(R.id.register_user_email_text);
        userPassword = findViewById(R.id.register_user_password_text);
        userRepPassword = findViewById(R.id.register_user_repeatpassword_text);

        userIsAdmin = findViewById(R.id.register_isadmin);

        register = findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fieldsOk()){
                    addUser();
                }else{
                    Toast.makeText(getApplicationContext(), "Por favor, revise todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    
    private boolean fieldsOk(){

        String fullName = userFullname.getText().toString();
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();
        String repeatpassword = userRepPassword.getText().toString();

        if(fullName.isEmpty()){
            return false;
        }else if(email.isEmpty() || EmailValidation.isInvalidEmail(email)){
            return false;
        }else return !password.isEmpty() && !repeatpassword.isEmpty() && password.equals(repeatpassword) && password.length() >= 6;

    }

    private void addUser(){
            fAuth.createUserWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString()).addOnSuccessListener(authResult -> {

                FirebaseUser user = fAuth.getCurrentUser();
                Toast.makeText(getApplicationContext(), "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                DocumentReference df = fStore.collection("Users").document(user.getUid());

                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("FullName", userFullname.getText().toString());
                userInfo.put("UserEmail", userEmail.getText().toString());

                boolean isAdmin = userIsAdmin.isChecked();

                if(isAdmin) {
                    userInfo.put("isAdmin", "1");
                }else{
                    userInfo.put("isAdmin", "0");
                }

                df.set(userInfo).addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Error al añadir el nuevo usuario", Toast.LENGTH_SHORT).show();
                });

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error al crear la cuenta", Toast.LENGTH_SHORT).show());
    }
    

    public void goToLogin(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}
