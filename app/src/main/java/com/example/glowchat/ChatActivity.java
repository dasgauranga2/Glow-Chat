package com.example.glowchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {

    EditText message;
    String user1;
    String user2;
    FirebaseDatabase database;
    DatabaseReference ref;
    String SEP = "--**--";
    Intent intent;
    TextView chatuser;
    ImageView chatuser_img;
    ArrayList<String> chat_messages;

    Uri image_uri;
    //String download_url;
    private static final int IMAGE_REQUEST = 2;

    // function to send a message to another user
    public void add_message(View view) {

        // get the input from the user
        String user_message = message.getText().toString();
        if (user_message.length() > 0) {
            // get database reference from Firebase and the data to Firebase
            ref.child(user1 + "-" + user2).push().setValue(user_message+SEP+user1+SEP+user2);
            ref.child(user2 + "-" + user1).push().setValue(user_message+SEP+user1+SEP+user2);
        }

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

    // function to send an image to another user
    public void add_image(View view) {

        // launch an intent to open the gallery
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the image was successfully retrieved
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            // get the image data in uri format
            image_uri = data.getData();
            // upload the image to Firebase database
            upload_image();
        }
    }

    private void upload_image() {

        if (image_uri !=  null) {
            // set the database reference
            // set the directory and file name
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(user2).child(user1+"_"+System.currentTimeMillis()+".jpg");
            // upload the uri image
            storageReference.putFile(image_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // get the download url for the image
                            //String url = uri.toString();
                            // set the download url of the image
                            //download_url = url;
                            Toast.makeText(ChatActivity.this, "IMAGE SENT SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                            // download the image
                            //download_image();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChatActivity.this, "FAILED TO SEND IMAGE", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    // function to view the profile
    public void view_profile(View view) {
        Intent profile_intent = new Intent(ChatActivity.this, ViewProfileActivity.class);
        profile_intent.putExtra("email", intent.getStringExtra("user2"));
        startActivity(profile_intent);
    }

    // set the avatar of the current user
    public void get_current_user_avatar(String em) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference rf = db.getReference("AVATARS");

        rf.child(em.split("@")[0]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int resid = getResources().getIdentifier(snapshot.getValue().toString(),"drawable",getPackageName());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resid);
                chatuser_img.setImageBitmap(bitmap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        intent = getIntent();
        message = findViewById(R.id.messageInput);
        chatuser = findViewById(R.id.chatuserText);
        chatuser_img = findViewById(R.id.chatuserImage);
        // array list that will contain all the chat messages
        chat_messages = new ArrayList<String>();
        // user1 is the current user logged in
        user1 = intent.getStringExtra("user1").split("@")[0];
        // user2 is the user with whom user1 is currently chatting with
        user2 = intent.getStringExtra("user2").split("@")[0];
        // chatuser is the username of the user2
        chatuser.setText(intent.getStringExtra("chatuser_username"));
        // set the avatar of user2
        get_current_user_avatar(intent.getStringExtra("user2"));

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
                // reset the recycler view
                setup_list();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // function to setup the recycler view
    public void setup_list() {
        Log.i("ALL_MESSAGES", chat_messages.toString());
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ChatAdapter chatAdapter = new ChatAdapter(this, chat_messages, intent.getStringExtra("user1").split("@")[0]);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}