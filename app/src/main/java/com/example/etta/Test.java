package com.example.etta;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.etta.NavigationDrawerConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Test extends AppCompatActivity {
    private View navHeader;
    DatabaseReference databaseReference,databaseReferencePost;
    private List<User> lp;
    private ProgressBar uploadbar;
    private FirebaseAuth firebaseAuth;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        new Handler();
        firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        uploadbar=findViewById(R.id.upload_progressBar);

        if (uploadbar!=null){
            uploadbar.setVisibility(View.GONE);
        }
        lp = new ArrayList<>();
        final RecyclerView rv=findViewById(R.id.user_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uploadbar.setVisibility(View.VISIBLE);
                for (DataSnapshot posts:dataSnapshot.getChildren()) {
                    User post = posts.getValue(User.class);
                     if (!post.getUid().equalsIgnoreCase(user.getUid())){
                    lp.add(post);
                    }
                }

                UserAdapter ma=new UserAdapter(Test.this,lp);

                rv.setAdapter(ma);
                uploadbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Test.this, "Erro "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}
