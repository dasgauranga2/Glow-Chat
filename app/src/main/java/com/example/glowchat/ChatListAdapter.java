package com.example.glowchat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> emails;
    ArrayList<String> usernames;
    String current_user;

    // the context and the data is passed to the adapter
    public ChatListAdapter(Context ct, ArrayList<String> un, ArrayList<String> em, String cu) {
        context = ct;
        emails = em;
        usernames = un;
        current_user = cu;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // The 'chat_list_row.xml' file in layout folder defines
        // the style for each row
        View view = inflater.inflate(R.layout.chat_list_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // use the data passed to the adapter above
        // and set the data to the text view below
        holder.username.setText(usernames.get(position));

        // detect if an item is clicked using the root layout of the 'chat_list_row.xml' file
        holder.main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("user1",current_user);
                intent.putExtra("user2",emails.get(position));
                intent.putExtra("chatuser_username",usernames.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // return the length of the recycler view
        return usernames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        ConstraintLayout main_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.usernameText);
            main_layout = itemView.findViewById(R.id.chat_list_layout);
        }
    }
}