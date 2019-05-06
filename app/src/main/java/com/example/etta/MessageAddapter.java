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

public class MessageAddapter extends RecyclerView.Adapter<MessageAddapter.ViewHolder> {
public List<Message> postList;
private Context context;
        CheckBox cb;
        String user;

public MessageAddapter( Context context,  List <Message> posts,String user) {
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
    public MessageAddapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater lf=LayoutInflater.from(context);
        View card= lf.inflate(R.layout.chat_right,viewGroup,false);
        return new MessageAddapter.ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(MessageAddapter.ViewHolder viewHolder, int i) {
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        cb=new CheckBox(context.getApplicationContext());
//        cb.setText("select");
//        cb.setId(i);
//        cb.setLayoutParams(params);
        Message message = postList.get(i);
        String sender=message.getEmail();
        viewHolder.name.setText("From: "+sender+"\n"+message.getMessage());



    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}

