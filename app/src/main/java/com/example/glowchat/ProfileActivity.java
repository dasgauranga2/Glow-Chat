package com.example.glowchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfileActivity extends AppCompatActivity {

    EditText username,status;
    FirebaseDatabase database;
    DatabaseReference username_ref,status_ref;
    String current_user;

    public void save_profile(View view) {

        String un = username.getText().toString();
        String st = status.getText().toString();

        if (un.length() > 0) {
            username_ref.child(current_user).setValue(un);
        }
        if (st.length() > 0) {
            status_ref.child(current_user).setValue(st);
        }

        Intent intent = new Intent(ProfileActivity.this, ChatListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.usernameTextInput);
        status =  findViewById(R.id.statusTextInput);
        database = FirebaseDatabase.getInstance();
        username_ref = database.getReference("USERNAMES");
        status_ref = database.getReference("STATUS");

        Intent intent = getIntent();
        current_user = intent.getStringExtra("current_user").split("@")[0];
    }
}