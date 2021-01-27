package com.example.glowchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
class Message {

    public String u1;
    public String u2;
    public String data;

    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Message(String u1, String u2, String data) {
        this.u1 = u1;
        this.u2 = u2;
        this.data = data;
    }
}

public class ChatActivity extends AppCompatActivity {

    EditText message;
    String user1;
    String user2;
    FirebaseDatabase database;
    DatabaseReference ref;

    public void add_message(View view) {

        //Contact c = new Contact(current_user,contact_name);
        //ref.setValue(user);
        //Message m = new Message(user1,user2,message.getText().toString());
        ref.child(user1 + "-" + user2).push().setValue(message.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Intent intent = getIntent();
        Intent intent = getIntent();
        message = findViewById(R.id.messageInput);
        user1 = intent.getStringExtra("user1").split("@")[0];
        user2 = intent.getStringExtra("user2").split("@")[0];

        database = FirebaseDatabase.getInstance();
        //ref = database.getReference("MESSAGES").child(user1 + "-" + user2);
        ref = database.getReference("MESSAGES");

        Log.i("MESSAGE_INFO", "TESTING");
        ref.child(user1 + "-" + user2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    Log.i("MESSAGE_INFO", s.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.i("MESSAGE_INFO", "TESTING");
        ref.child(user2 + "-" + user1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    Log.i("MESSAGE_INFO", s.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}