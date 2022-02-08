package com.example.upbeatproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private EditText etRegisterUsername, etRegisterPassword, etRegisterEmailId;
    private Button btnFirebaseRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etRegisterEmailId = findViewById(R.id.etRegisterEmailId);
        btnFirebaseRegister = findViewById(R.id.btnFirebaseRegister);

        mAuth = FirebaseAuth.getInstance();

        btnFirebaseRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etRegisterUsername.getText().toString().trim();
                String password = etRegisterPassword.getText().toString().trim();
                String emailId = etRegisterEmailId.getText().toString().trim();

                if (password.length() == 0 || password.length() > 30) {
                    Toast.makeText(RegistrationActivity.this, "Plz enter password again", Toast.LENGTH_SHORT).show();
                    etRegisterPassword.requestFocus();
                }

                if (password.length() == 0 || password.length() > 30) {
                    Toast.makeText(RegistrationActivity.this, "Plz enter password again", Toast.LENGTH_SHORT).show();
                    etRegisterPassword.requestFocus();
                }

                if ((emailId.length() == 0 || emailId.length() > 30) && !Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
                    Toast.makeText(RegistrationActivity.this, "Plz enter email Id again", Toast.LENGTH_SHORT).show();
                    etRegisterEmailId.requestFocus();
                }


                if ((username.length() > 0 && username.length() < 30 &&
                        password.length() > 0 && password.length() < 30
                        && emailId.length() > 0 && emailId.length() < 30) &&
                        Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
                    mAuth.createUserWithEmailAndPassword(emailId, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        User fireBaseUser = new User(username, password, emailId);

                                        FirebaseDatabase.getInstance().getReference("Authenticated_User")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(fireBaseUser)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(RegistrationActivity.this,
                                                                    "User has been registered",
                                                                    Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(RegistrationActivity.this,
                                                                    LoginActivity.class));
                                                            finish();
                                                        } else {
                                                            Toast.makeText(RegistrationActivity.this,
                                                                    "Failed to register",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(RegistrationActivity.this,
                                                "Failed to register",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}