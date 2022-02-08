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

public class LoginActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;
    EditText etLoginPassword, etLoginEmailId;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        etLoginEmailId = findViewById(R.id.etLoginEmailId);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = etLoginPassword.getText().toString().trim();
                String emailId = etLoginEmailId.getText().toString().trim();

                if ((emailId.length() == 0 || emailId.length() > 30) && !Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
                    Toast.makeText(LoginActivity.this, "Plz enter email Id again", Toast.LENGTH_SHORT).show();
                    etLoginEmailId.requestFocus();
                }

                if (password.length() == 0 || password.length() > 30) {
                    Toast.makeText(LoginActivity.this, "Plz enter password again", Toast.LENGTH_SHORT).show();
                    etLoginPassword.requestFocus();
                }

                if ((password.length() > 0 && password.length() < 30
                        && emailId.length() > 0 && emailId.length() < 30) &&
                        Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {

                    mAuth.signInWithEmailAndPassword(emailId, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();

                                        FirebaseDatabase.getInstance().getReference("Authenticated_User")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("emailId")
                                                .setValue(emailId);

                                        FirebaseDatabase.getInstance().getReference("Authenticated_User")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("password")
                                                .setValue(password);

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
}