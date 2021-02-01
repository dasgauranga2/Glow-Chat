package com.example.glowchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class AvatarActivity extends AppCompatActivity {

    Spinner spinner;
    ImageView profile;
    FirebaseDatabase database;
    DatabaseReference avatar_ref;
    String current_user;
    int current_position;
    ArrayList<String> choices;

    // save the user avatar to the Firebase database
    public void save_avatar(View view) {
        avatar_ref.child(current_user).setValue(choices.get(current_position));

        Intent cl_intent = new Intent(AvatarActivity.this, ChatListActivity.class);
        startActivity(cl_intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        profile = findViewById(R.id.profileImage);
        spinner = findViewById(R.id.profileImageChoices);

        database = FirebaseDatabase.getInstance();
        avatar_ref = database.getReference("AVATARS");

        Intent intent = getIntent();
        current_user = intent.getStringExtra("current_user").split("@")[0];

        // array list of string containing the image file names
        choices = new ArrayList<>(Arrays.asList("thor", "deadpool", "hulk", "brachiosaurus", "stegosaurus", "trex"));
        // setup the dropdown menu
        setup_dropdown_menu();

        // detect dropdown menu item select
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // get the resource id of the image that we have selected in the dropdown menu
                int resid = getResources().getIdentifier(choices.get(position),"drawable",getPackageName());
                // set the image view
                profile.setImageResource(resid);
                // set the current position
                current_position = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setup_dropdown_menu() {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choices);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }
}