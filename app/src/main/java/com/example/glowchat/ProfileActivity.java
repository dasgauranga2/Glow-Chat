package com.example.glowchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    EditText username;
    FirebaseDatabase database;
    DatabaseReference ref;
    String current_user;

    public void save_profile(View view) {

        String un = username.getText().toString();

        ref.child(current_user).setValue(un);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.usernameInput);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("USERNAMES");

        Intent intent = getIntent();
        current_user = intent.getStringExtra("current_user").split("@")[0];
    }
}