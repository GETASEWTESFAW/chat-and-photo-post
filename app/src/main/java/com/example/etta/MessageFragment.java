package com.example.etta;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private FirebaseAuth.AuthStateListener authListener;
    private  FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,databaseReferencePost;
    private List<Message> lp;
    private RecyclerView rview;
    private ProgressBar uploadbar;
    private SharedPreferences setting;
    FragmentTransaction ft;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rview=(RecyclerView)inflater.inflate(R.layout.recycler_view, container, false);
        lp = new ArrayList<>();
        uploadbar=getActivity().findViewById(R.id.upload_progressBar);
        uploadbar.setVisibility(View.VISIBLE);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rview.setLayoutManager(layoutManager);

        firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Messages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot posts:dataSnapshot.getChildren()) {
                    Message message = posts.getValue(Message.class);
                    if(!message.getSender().equalsIgnoreCase(user.getUid()) && message.getReceiver().equalsIgnoreCase(user.getUid())){
                        lp.add(message);
                    }

                }
                MessageAddapter ma=new MessageAddapter(getActivity(),lp,user.getUid());
                rview.setAdapter(ma);
                uploadbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Erro "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return rview;
    }


}
