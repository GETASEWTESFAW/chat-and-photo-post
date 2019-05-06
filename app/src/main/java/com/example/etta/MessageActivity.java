package com.example.etta;

import android.app.ActionBar;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private View navHeader;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,databaseReferencePost;
    FirebaseUser user;
    private ProgressBar uploadbar;
    private List<Message> lp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab=getActionBar();
        ab.setTitle("Massages");
        ab.setDisplayHomeAsUpEnabled(true);
        uploadbar=findViewById(R.id.upload_progressBar);
        if (uploadbar!=null){
            uploadbar.setVisibility(View.GONE);
        }
        user = firebaseAuth.getCurrentUser();
        lp = new ArrayList<>();
        final RecyclerView rv=findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        databaseReference= FirebaseDatabase.getInstance().getReference("Messages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploadbar.setVisibility(View.VISIBLE);
                for (DataSnapshot posts:dataSnapshot.getChildren()) {
                    Message message = posts.getValue(Message.class);
                    if((!message.getSender().equalsIgnoreCase(user.getUid())) || message.getReceiver().equalsIgnoreCase(user.getUid()) ){
                        lp.add(message);
                    }

                }

                MessageAddapter ma=new MessageAddapter(MessageActivity.this,lp,user.getUid());

                rv.setAdapter(ma);
                uploadbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MessageActivity.this, "Erro "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                uploadbar.setVisibility(View.GONE);
            }
        });
    }
}
