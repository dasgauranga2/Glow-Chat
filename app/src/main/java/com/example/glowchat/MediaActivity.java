package com.example.glowchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MediaActivity extends AppCompatActivity {

    Intent intent;
    String current_user;
    RecyclerView recyclerView;
    ArrayList<Bitmap> images;
    ArrayList<String> image_names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        intent = getIntent();
        current_user = intent.getStringExtra("current_user");

        recyclerView = findViewById(R.id.recyclerView);

        // get the database reference to retrieve the images
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(current_user.split("@")[0]);
        // list method returns all the images in the directory
        Task<ListResult> listPageTask = storageReference.list(100);
        // retrieve all the images
        listPageTask
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        images = new ArrayList<Bitmap>();
                        image_names = new ArrayList<>();
                        // list of database references
                        // each database reference refers to one image file
                        List<StorageReference> items = listResult.getItems();
                        // iterate over the list
                        for (StorageReference item : items) {
                            // get the image in byte array format
                            item.getBytes(5000000).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    // create a bitmap image
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                                    images.add(bitmap);
                                    image_names.add(item.getName());

                                    setup_list();
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MediaActivity.this, "MEDIA FAILURE", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // setup the recycler view
    public void setup_list() {
        MediaAdapter mediaAdapter = new MediaAdapter(this, images, image_names);
        recyclerView.setAdapter(mediaAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}