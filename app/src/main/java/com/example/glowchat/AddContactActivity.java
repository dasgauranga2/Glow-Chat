package com.example.glowchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.type.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddContactActivity extends AppCompatActivity {

    Button add;
    EditText contact;
    FirebaseAuth auth;

    public void add_contact(View view) {
        String contact_name = contact.getText().toString();
        String current_user = auth.getCurrentUser().getEmail().split("@")[0];
        Toast.makeText(AddContactActivity.this, current_user, Toast.LENGTH_SHORT).show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("CONTACT LIST");
        //Contact c = new Contact(current_user,contact_name);
        //ref.setValue(user);
        ref.child(current_user).push().setValue(contact_name);

        Intent intent = new Intent(AddContactActivity.this, ChatListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        contact = findViewById(R.id.contactNameInput);
        add = findViewById(R.id.addButton);
        auth = FirebaseAuth.getInstance();
    }
}