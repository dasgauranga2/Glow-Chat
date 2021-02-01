package com.example.glowchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ChatListActivity extends AppCompatActivity {

    ArrayList<String> emails,usernames;
    ArrayList<Bitmap> avatars;
    FirebaseAuth auth;
    String current_user;

    // add new contact
    public void add_contact(View view) {
        Intent intent = new Intent(ChatListActivity.this, AddContactActivity.class);
        startActivity(intent);
    }

    // edit the profile
    public void edit_profile(View view) {
        Intent intent = new Intent(ChatListActivity.this, ProfileActivity.class);
        intent.putExtra("current_user",current_user);
        startActivity(intent);
    }

    // edit the avatar of the user
    public void edit_avatar(View view) {
        Intent intent = new Intent(ChatListActivity.this, AvatarActivity.class);
        intent.putExtra("current_user",current_user);
        startActivity(intent);
    }

    // get the username of an user given the email
    public void get_username(String em) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference rf = db.getReference("USERNAMES");

        rf.child(em.split("@")[0]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usernames.add(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // get the avatar of an user given the email
    public void get_avatar(String em) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference rf = db.getReference("AVATARS");

        rf.child(em.split("@")[0]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int resid = getResources().getIdentifier(snapshot.getValue().toString(),"drawable",getPackageName());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resid);
                avatars.add(bitmap);
                if (avatars.size() == emails.size()) {
                    setup_list();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        auth = FirebaseAuth.getInstance();
        current_user = auth.getCurrentUser().getEmail();
        emails = new ArrayList<String>();
        usernames = new ArrayList<String>();
        avatars = new ArrayList<Bitmap>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("CONTACT LIST").child(current_user.split("@")[0]);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                emails = new ArrayList<String>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    String contact_email = s.getValue().toString();
                    emails.add(contact_email);
                    get_username(contact_email);
                    get_avatar(contact_email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // setup the recycler view
    public void setup_list() {

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ChatListAdapter chat_list_adapter = new ChatListAdapter(this, usernames, emails, current_user, avatars);
        recyclerView.setAdapter(chat_list_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}