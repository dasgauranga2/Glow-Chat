package com.example.glowchat;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {

    ArrayList<String> chats;
    Context context;
    String current_user;
    String SEP = "--**--";

    // the context and the data is passed to the adapter
    public ChatAdapter(Context ct, ArrayList<String> chat_messages, String curr_user) {
        context = ct;
        chats = chat_messages;
        current_user = curr_user;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // The 'row.xml' file in layout folder defines
        // the style for each row
        View view = inflater.inflate(R.layout.chat_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String[] full_message = chats.get(position).split(Pattern.quote(SEP));
        int message_length = full_message[0].length();

        Log.i("MESSAGE_INFO", Arrays.toString(full_message));
        if (full_message[2].equals(current_user)) {
            Log.i("MESSAGE_INFO", "FIRST USER");
            holder.chat1.setVisibility(View.VISIBLE);
            holder.chat1.setText(full_message[0]);
            holder.chat1.setBackgroundResource(R.drawable.chat_background);
            holder.chat1.setMaxWidth(550);
            holder.chat2.setVisibility(View.GONE);
//            holder.chat2.setText("");
        }
        else if (full_message[1].equals(current_user)) {
            Log.i("MESSAGE_INFO", "SECOND USER");
            holder.chat2.setVisibility(View.VISIBLE);
            holder.chat2.setText(full_message[0]);
            holder.chat2.setBackgroundResource(R.drawable.chat_background);
            holder.chat2.setMaxWidth(550);
            holder.chat1.setVisibility(View.GONE);
//            holder.chat1.setText("");
        }
    }

    @Override
    public int getItemCount() {
        // return the length of the recycler view
        return chats.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        // Each row will contain two text views
        // for displaying the data
        TextView chat1,chat2;
        // Select the root layout of the 'row.xml' file
        //ConstraintLayout main_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Set the text view from the 'row.xml' file in layout folder
            chat1 = itemView.findViewById(R.id.chat_text1);
            chat2 = itemView.findViewById(R.id.chat_text2);
        }
    }
}