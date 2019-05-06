package com.example.etta;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class ChatAddapter extends RecyclerView.Adapter<ChatAddapter.ViewHolder> {
    public List<Message> postList;
    private Context context;
    CheckBox cb;
    String user;
    private static final int RIGT=0;
    private static final int LEFT=1;
    public ChatAddapter( Context context,  List <Message> posts,String user) {
        this.context=context;
        this.postList = posts;
        this.user=user;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.user_name);
        }
    }

    @NonNull
    @Override
    public ChatAddapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater lf=LayoutInflater.from(context);
        if (i==RIGT){
            View card=  lf.inflate(R.layout.chat_right,viewGroup,false);
            return new ChatAddapter.ViewHolder(card);
        }
        else {
            View card=  lf.inflate(R.layout.chat_left,viewGroup,false);
            return new ChatAddapter.ViewHolder(card);
        }


    }

    @Override
    public void onBindViewHolder(ChatAddapter.ViewHolder viewHolder, int i) {
        Message message = postList.get(i);
        String sender=message.getSender();
        viewHolder.name.setText(message.getMessage());

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (postList.get(position).getSender().equalsIgnoreCase(user)){
            return LEFT;
        }
        else {
            return RIGT;
        }

    }
}

