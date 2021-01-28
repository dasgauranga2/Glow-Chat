package com.example.glowchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ChatActivity extends AppCompatActivity {

    EditText message;
    String user1;
    String user2;
    FirebaseDatabase database;
    DatabaseReference ref;
    String SEP = "--**--";
    Intent intent;

    ArrayList<String> chat_messages;

    // function to send a message to another user
    public void add_message(View view) {

        // get database reference from Firebase and the data to Firebase
        ref.child(user1 + "-" + user2).push().setValue(message.getText().toString()+SEP+user1+SEP+user2);
        ref.child(user2 + "-" + user1).push().setValue(message.getText().toString()+SEP+user1+SEP+user2);

        // clear the message input box
        message.setText("");
        // hide the keyboard
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

        intent = getIntent();
        message = findViewById(R.id.messageInput);
        // array list that will contain all the chat messages
        chat_messages = new ArrayList<String>();
        // user1 is the current user logged in
        user1 = intent.getStringExtra("user1").split("@")[0];
        // user2 is the user with whom user1 is currently chatting with
        user2 = intent.getStringExtra("user2").split("@")[0];

        // get the database reference from Firebase
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("MESSAGES");

        // read data from Firebase
        // get all chat messages between user1 and user2
        ref.child(user1 + "-" + user2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // clear the array list
                chat_messages.clear();
                // we iterate over all chat messages
                for (DataSnapshot s : snapshot.getChildren()) {
                    // insert data into the array list
                    chat_messages.add(s.getValue().toString());
                }
                // reset the list view
                setup_list();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // function to setup the list view
    public void setup_list() {
//        ListView listView = findViewById(R.id.chatList);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,chat_messages);
//        listView.setAdapter(arrayAdapter);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ChatAdapter chatAdapter = new ChatAdapter(this, chat_messages, intent.getStringExtra("user1").split("@")[0]);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}