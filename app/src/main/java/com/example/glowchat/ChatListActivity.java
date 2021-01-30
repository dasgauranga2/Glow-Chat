package com.example.glowchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
    FirebaseAuth auth;
    String current_user;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        Intent intent;

        switch (item.getItemId()) {
            case R.id.add_contact:
                //Toast.makeText(getApplicationContext(),"ADD CONTACT", Toast.LENGTH_SHORT).show();
                intent = new Intent(ChatListActivity.this, AddContactActivity.class);
                startActivity(intent);
                return true;
            case R.id.profile:
                intent = new Intent(ChatListActivity.this, ProfileActivity.class);
                intent.putExtra("current_user",current_user);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    public void add_data(String s) {
        usernames.add(s);
    }

    public void get_username(String em) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference rf = db.getReference("USERNAMES");
        String[] result = new String[1];
        result[0] = "TESTING";

        rf.child(em.split("@")[0]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usernames.add(snapshot.getValue().toString());
                //Log.i("CONTACT_INFO", usernames.toString() + " " + emails.toString());
                //Toast.makeText(ChatListActivity.this, result[0], Toast.LENGTH_SHORT).show();
                if (usernames.size() == emails.size()) {
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void setup_list() {
        ListView listView = findViewById(R.id.listView);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,usernames);
        // link the list view to the array adapter
        listView.setAdapter(arrayAdapter);

        // detect list view click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // function is called when a list view item is clicked
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String user1 = current_user;
                String user2 = emails.get(position);
                //Toast.makeText(getApplicationContext(), message_id,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
                intent.putExtra("user1",user1);
                intent.putExtra("user2",user2);
                startActivity(intent);
            }
        });
    }
}