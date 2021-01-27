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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatListActivity extends AppCompatActivity {

    ArrayList<String> list;
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

        switch (item.getItemId()) {
            case R.id.add_contact:
                //Toast.makeText(getApplicationContext(),"ADD CONTACT", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChatListActivity.this, AddContactActivity.class);
                startActivity(intent);
                return true;
            case R.id.profile:
                Toast.makeText(getApplicationContext(),"PROFILE",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

//        setup_list();
        auth = FirebaseAuth.getInstance();
        current_user = auth.getCurrentUser().getEmail();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("CONTACT LIST");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<String>();
                //Toast.makeText(ChatListActivity.this,snapshot.toString(),Toast.LENGTH_SHORT).show();
                //Log.i("CONTACT_LIST",snapshot.toString());
                for (DataSnapshot s : snapshot.getChildren()) {
                    Contact c = s.getValue(Contact.class);
                    //Contact c = (Contact)s.getValue();
                    //HashMap<String,String> map = (HashMap<String, String>)s.getValue();
                    //HashMap<String,String> info = s.getValue();
                    if (current_user.equals(c.username)) {
                        //Log.i("CONTACT_INFO", c.contactname);
                        list.add(c.contactname);
                    }
                    //list = new ArrayList<String>();
                    //list.add(s.getValue());
                }
                setup_list();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setup_list() {
        ListView listView = findViewById(R.id.listView);

//        ArrayList<String> list = new ArrayList<String>();
//        list.add("great");
//        list.add("hello");
//        list.add("man");
//        list.add("awesome");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        // link the list view to the array adapter
        listView.setAdapter(arrayAdapter);

        // detect list view click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // function is called when a list view item is clicked
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // the list view items have the same ordering as the ArrayList above
                // 'position' is the index of the list view item clicked
                String user1 = current_user;
                String user2 = list.get(position);
                //Toast.makeText(getApplicationContext(), message_id,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
                intent.putExtra("user1",user1);
                intent.putExtra("user2",user2);
                startActivity(intent);
            }
        });
    }
}