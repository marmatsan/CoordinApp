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
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    LinearProgressIndicator logIndicator;
    TextInputEditText userFullname, userEmail, userPassword, userRepPassword;
    MaterialButton register;
    SwitchMaterial userIsAdmin;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /*
         * fAuth is used for creating the new user given their email and password, and
         * fStore is used to get the "Users" collection and store the information of the user in the
         * Firebase Firestore.
         */
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userFullname = findViewById(R.id.register_user_fullname_text);
        userEmail = findViewById(R.id.register_user_email_text);
        userPassword = findViewById(R.id.register_user_password_text);
        userRepPassword = findViewById(R.id.register_user_repeatpassword_text);

        userIsAdmin = findViewById(R.id.register_isadmin);

        logIndicator = findViewById(R.id.linearProgressIndicatoRegister);
        logIndicator.setVisibility(View.GONE);

        register = findViewById(R.id.register_button);
        register.setOnClickListener(view -> {
            if(fieldsOk()){
                logIndicator.setVisibility(View.VISIBLE);
                addUser();
            }else{
                Toast.makeText(getApplicationContext(), "Por favor, revise todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * Check whether the fields given as input by the user at login are OK.
     * The fields are OK if the fiels are not empty, the email has valid format and the password
     * and the repeated password are equal (and have a length greater than 6 as requierd by FirebaseAuth).
     *
     * @return Whether the fields are valid.
     *
     */
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

    /**
     * Creates the new user and adds them to the Firebase Firestore, specifically to the collection called "Users".
     * The document created in the Firestore is the uid of the user (specified in fAuth.getCurrentUser().getUid()).
     * The created user has the following data fields:
     *
     * FullName: Name and surname of the user.
     * UserEmail: The email of the user, required for loggin in
     * isAdmin: Takes the value 1 if the user is administrator and 0 if it is not. It is specified by the state of the switch SwitchMaterial userIsAdmin.
     *
     * After adding the user, the user is sent to the LoginActivity for logging.
     */
    private void addUser(){
            fAuth.createUserWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString()).addOnSuccessListener(authResult -> {

                FirebaseUser user = fAuth.getCurrentUser();
                Toast.makeText(getApplicationContext(), "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();

                Map<String, Object> userInfo = new HashMap<String, Object>();
                userInfo.put("FullName", userFullname.getText().toString());
                userInfo.put("UserEmail", userEmail.getText().toString());

                DocumentReference df;
                boolean isAdmin = userIsAdmin.isChecked();

                if(isAdmin) {
                    df = fStore.collection("Teachers").document(user.getUid());
                }else{
                    df = fStore.collection("Students").document(user.getUid());
                }

                df.set(userInfo).addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Error al añadir el nuevo usuario", Toast.LENGTH_SHORT).show();
                });

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

            }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "Error al crear la cuenta", Toast.LENGTH_SHORT).show());
    }

    /**
     * Used by the gotologin TextView declared at activity_register.xml to go to LoginActivity.
     * @param view The TextView.
     */
    public void goToLogin(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}
