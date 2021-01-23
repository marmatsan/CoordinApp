package com.elcazadordebaterias.coordinapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.elcazadordebaterias.coordinapp.R;
import com.elcazadordebaterias.coordinapp.utils.EmailValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.BaseProgressIndicator;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    LinearProgressIndicator logIndicator;
    TextInputEditText userEmail, userPassword;
    MaterialButton login;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
         * fAuth is used to sign in provided email and password in the logging button listener and fStore
         * is used in the checkUserAccessLevel() method of this class.
         */
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        logIndicator = findViewById(R.id.linearProgressIndicatorLogin);
        logIndicator.setVisibility(View.GONE);

        // Texts that contain the user email and user password required for login.
        userEmail = findViewById(R.id.login_user_email_text);
        userPassword = findViewById(R.id.login_user_password_text);

        // Button for login.
        login = findViewById(R.id.login_button);
        login.setOnClickListener(view -> {
            if (fieldsOk()) {
                logIndicator.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(userEmail.getText().toString(), userPassword.getText().toString()).addOnSuccessListener(authResult -> {
                    Toast.makeText(getApplicationContext(), "Inicio de sesión correcto", Toast.LENGTH_SHORT).show();
                    checkUserAccessLevel(authResult.getUser().getUid());
                }).addOnFailureListener(e -> {
                    Toast.makeText(getApplicationContext(), "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(getApplicationContext(), "Por favor, revisa los campos", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity_Student.class));
            finish();
        }
    }

    /**
     * Check whether the fields given as input by the user at login are OK.
     * The fields are OK if the email input has email format and if both email and password
     * are not empty.
     * @return Whether the fields are valid.
     */
    private boolean fieldsOk() {

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (email.isEmpty() || EmailValidation.isInvalidEmail(email)) {
            return false;
        } else return !password.isEmpty();

    }

    /**
     * Check the access level of the user. The user is redirected to their corresponding
     * activity based on which database belongs.
     * It uses an instance of FirebaseFirestore to check the permissions of the user identified with
     * the uid parameter.
     *
     * @param uid The Unique ID of the user. Used to identify the current user that is being logging in.
     *
     */
    private void checkUserAccessLevel(String uid) {

        DocumentReference df = fStore.collection("Teachers").document(uid);

        df.get().addOnCompleteListener(task -> { // TODO: 23-01-2021 Chech the robustness of this
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    startActivity(new Intent(getApplicationContext(), MainActivity_Teacher.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), MainActivity_Student.class));
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error comprobando los permisos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Used by the gotoregister TextView declared at activity_login.xml to go to RegisterActivity.
     * @param view The TextView.
     */
    public void goToRegister(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }

}
