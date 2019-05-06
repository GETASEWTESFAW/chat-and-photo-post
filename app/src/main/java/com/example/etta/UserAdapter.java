package com.example.etta;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    public List<User> postList;
    private Context context;
    CheckBox cb;

    public UserAdapter( Context context,  List <User> posts) {
        this.context=context;
        this.postList = posts;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.user_name);
            cardView=(CardView) itemView;
        }
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater lf=LayoutInflater.from(context);
        CardView card= (CardView) lf.inflate(R.layout.user_list,viewGroup,false);

        return new UserAdapter.ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder viewHolder, int i) {
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        cb=new CheckBox(context.getApplicationContext());
//        cb.setText("select");
//        cb.setId(i);
//        cb.setLayoutParams(params);

        final User user = postList.get(i);
        viewHolder.name.setText(user.getName());
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ChatActivity.class);
                intent.putExtra("reciever",user.getUid());
                intent.putExtra("name",user.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
