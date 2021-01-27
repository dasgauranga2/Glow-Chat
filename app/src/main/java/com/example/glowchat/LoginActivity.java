package com.example.glowchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Button login;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // get the email entered by the user
        email = findViewById(R.id.emailLoginInput);
        // get the password entered by the user
        password = findViewById(R.id.passwordLoginInput);
        login = findViewById(R.id.loginConfirmButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_text = email.getText().toString();
                String password_text = password.getText().toString();

                login_user(email_text,password_text);
            }
        });
    }

    public void login_user(String e, String p) {
        auth = FirebaseAuth.getInstance();
        // login the user using email and password
        auth.signInWithEmailAndPassword(e,p).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent intent = new Intent(LoginActivity.this, ChatListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}