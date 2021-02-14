package com.example.glowchat;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MyViewHolder> {

    Context context;
    ArrayList<Bitmap> images;
    ArrayList<String> image_names;

    // the context and the data is passed to the adapter
    public MediaAdapter(Context ct, ArrayList<Bitmap> imgs, ArrayList<String> img_names) {
        context = ct;
        images = imgs;
        image_names = img_names;
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
        // and set the data to the image view below
        holder.text.setText(image_names.get(position).split("_")[0]);
        holder.image.setImageBitmap(images.get(position));
        // if an image is clicked we want to
        // display the image in full screen
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MediaDetailActivity.class);
                // we create a file and
                // save the image to the file
                save_bitmap(images.get(position));
                context.startActivity(intent);
            }
        });
    }

    public void save_bitmap(Bitmap bitmap) {
        ContextWrapper wrapper = new ContextWrapper(context);
        File file = wrapper.getDir("IMAGES",MODE_PRIVATE);
        File image_file = new File(file, "image_detail.jpg");
        try {
            // use an output stream to write data
            OutputStream stream = null;
            // an output stream that writes bytes to a file
            stream = new FileOutputStream(image_file);
            // write a compressed version of the bitmap to the specified output stream
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            stream.flush();
            stream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        // return the length of the recycler view
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        // Each row will contain one image
        ImageView image;
        TextView text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Set the image view from the 'media_row.xml' file in layout folder
            image = itemView.findViewById(R.id.mediaImage);
            text = itemView.findViewById(R.id.mediaUsername);
        }
    }
}