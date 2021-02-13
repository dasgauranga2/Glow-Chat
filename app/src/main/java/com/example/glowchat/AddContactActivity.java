package com.example.glowchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddContactActivity extends AppCompatActivity {

    Button add;
    EditText contact;
    FirebaseAuth auth;

    public void check_email(String email, String cu) {

        EmailValidator ev = new EmailValidator();
        // check if valid email address is entered
        if (!ev.validateEmail(email)) {
            Toast.makeText(getApplicationContext(), "INVALID EMAIL ADDRESS", Toast.LENGTH_SHORT).show();
            return;
        }

        // check if email address belongs to the current user
        if (email.equals(auth.getCurrentUser().getEmail())) {
            Toast.makeText(getApplicationContext(), "CANNOT ENTER EMAIL ADDRESS OF CURRENT USER", Toast.LENGTH_SHORT).show();
            return;
        }

        // get the database reference from Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("CONTACT LIST");

        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        // check if we can sign in using the email
                        boolean is_new_user = task.getResult().getSignInMethods().isEmpty();
                        // array list of contacts of the current user
                        ArrayList<String> ems = getIntent().getStringArrayListExtra("current_user_contacts");

                        // check if email address exists or not
                        if (is_new_user) {
                            Toast.makeText(getApplicationContext(), "EMAIL ADDRESS DOES NOT EXISTS", Toast.LENGTH_SHORT).show();
                            return;
                        } // check if email already exists in contact list
                        else if (ems.contains(email)) {
                            Toast.makeText(getApplicationContext(), "EMAIL ADDRESS ALREADY EXISTS IN CONTACT LIST", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            // add the contact name to Firebase
                            ref.child(cu).push().setValue(email);

                            // go back to chat list activity
                            Intent intent = new Intent(AddContactActivity.this, ChatListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

    }

    // function to add a contact
    public void add_contact(View view) {
        // get the contact name entered by the user
        String contact_name = contact.getText().toString();
        // get the username of the logged in user
        String current_user = auth.getCurrentUser().getEmail().split("@")[0];

        // check if email address is valid and then
        // add the contact name to Firebase
        check_email(contact_name,current_user);
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