package com.example.glowchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewProfileActivity extends AppCompatActivity {

    TextView username,email,status;
    String chatuser;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        username = findViewById(R.id.usernameViewText);
        email = findViewById(R.id.emailViewText);
        status = findViewById(R.id.statusViewText);

        Intent intent = getIntent();
        chatuser = intent.getStringExtra("email");
        email.setText(chatuser);

        database = FirebaseDatabase.getInstance();

        get_username();
        get_status();
    }

    public void get_username() {
        DatabaseReference username_ref = database.getReference("USERNAMES");
        username_ref.child(chatuser.split("@")[0]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                username.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void get_status() {
        DatabaseReference status_ref = database.getReference("STATUS");
        status_ref.child(chatuser.split("@")[0]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                status.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}