package com.example.glowchat;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MyViewHolder> {

    Context context;
    ArrayList<Bitmap> images;

    // the context and the data is passed to the adapter
    public MediaAdapter(Context ct, ArrayList<Bitmap> imgs) {
        context = ct;
        images = imgs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // The 'media_row.xml' file in layout folder defines
        // the style for each row
        View view = inflater.inflate(R.layout.media_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // use the data passed to the adapter above
        // and set the data to the text views below
        holder.image.setImageBitmap(images.get(position));
    }

    @Override
    public int getItemCount() {
        // return the length of the recycler view
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        // Each row will contain two text views
        // for displaying the data
        ImageView image;
        // Select the root layout of the 'row.xml' file
        //ConstraintLayout main_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Set the text view from the 'media_row.xml' file in layout folder
            image = itemView.findViewById(R.id.mediaImage);
            //main_layout = itemView.findViewById(R.id.row_layout);
        }
    }
}