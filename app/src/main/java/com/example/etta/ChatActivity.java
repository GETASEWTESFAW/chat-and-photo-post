package com.example.etta;

import android.app.ActionBar;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ChatActivity extends AppCompatActivity {
    private View navHeader;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser user;
    private List<Message> lp;
    private ProgressBar uploadbar;
    private Button btn;
    private EditText messageInput;
    private  String reciever,messageStr,recieverName;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        reciever=getIntent().getStringExtra("reciever");
        recieverName=getIntent().getStringExtra("name");
        uploadbar=findViewById(R.id.upload_progressBar);

        handler=new Handler();
        if (uploadbar!=null){
            uploadbar.setVisibility(View.GONE);
        }
        lp = new ArrayList<>();
        final RecyclerView rv=findViewById(R.id.chat_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        firebaseAuth=FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Messages");
        btn=findViewById(R.id.message_send);
        messageInput=findViewById(R.id.message_input);
        messageStr=messageInput.getText().toString();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploadbar.setVisibility(View.VISIBLE);
                lp.clear();
                for (DataSnapshot messages:dataSnapshot.getChildren()) {
                    Message message = messages.getValue(Message.class);
                   if (!reciever.isEmpty()) {
                       if (message.getSender().equals(user.getUid()) && message.getReceiver().equals(reciever)||message.getSender().equals(reciever) && message.getReceiver().equals(user.getUid())) {
                           lp.add(message);
                       }
                   }
                }

                ChatAddapter ma=new ChatAddapter(ChatActivity.this,lp,user.getUid());

                rv.setAdapter(ma);
                uploadbar.setVisibility(View.GONE);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Erro "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageInput.getText().toString().isEmpty()){
                    Toast.makeText(ChatActivity.this,"please write the message",Toast.LENGTH_SHORT);
                }
                else {

                    uploadbar.setVisibility(View.VISIBLE);
                    Message message = new Message(user.getUid(), reciever, messageInput.getText().toString(), user.getEmail());
                    String child = databaseReference.push().getKey();
                    databaseReference.child(child).setValue(message);
                    messageInput.setText("");
                    uploadbar.setVisibility(View.GONE);
                    Toast.makeText(ChatActivity.this, "message is sent", Toast.LENGTH_SHORT);
                }
            }
        });


    }
}
