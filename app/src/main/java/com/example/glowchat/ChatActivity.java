package com.example.glowchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;

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
    String SEP = "-**-";

    ArrayList<String> chat_messages;

    public void add_message(View view) {

        ref.child(user1 + "-" + user2).push().setValue(message.getText().toString()+SEP+user1+SEP+user2);
        ref.child(user2 + "-" + user1).push().setValue(message.getText().toString()+SEP+user1+SEP+user2);
        message.setText("");

        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
                chat_messages = new ArrayList<String>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    //Log.i("MESSAGE_INFO", s.getValue().toString());
                    chat_messages.add(s.getValue().toString());
                }
                setup_list();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setup_list() {
        ListView listView = findViewById(R.id.chatList);
        //chat_messages = new ArrayList<String>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,chat_messages);
        // link the list view to the array adapter
        listView.setAdapter(arrayAdapter);
    }
}