package com.example.etta;

import android.content.Context;
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

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    public List<Post> postList;
    private Context context;
    CheckBox cb;

    public MenuAdapter( Context context,  List <Post> posts) {
     this.context=context;
     this.postList = posts;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout linearLayout;
        ImageView cardImage;
        TextView cardText;

        public ViewHolder(View itemView) {
            super(itemView);
            cardImage=(ImageView)itemView.findViewById(R.id.card_image);
            cardText=(TextView)itemView.findViewById(R.id.card_text);
            linearLayout=itemView.findViewById(R.id.card_linear);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater lf=LayoutInflater.from(context);
        CardView card= (CardView) lf.inflate(R.layout.card_view,viewGroup,false);
        return new ViewHolder(card);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        cb=new CheckBox(context.getApplicationContext());
//        cb.setText("select");
//        cb.setId(i);
//        cb.setLayoutParams(params);

        Post post = postList.get(i);
        viewHolder.cardText.setText(post.getPost());
        if (!post.getUrl().isEmpty()){
            Glide.with(context).load(post.getUrl()).into(viewHolder.cardImage);
        }
        else {
            viewHolder.cardImage.setImageResource(R.drawable.etta);
        }
//        viewHolder.linearLayout.addView(cb);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
