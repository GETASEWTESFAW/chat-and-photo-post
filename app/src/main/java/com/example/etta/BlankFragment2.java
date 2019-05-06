package com.example.etta;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment2 extends Fragment {
      private Button btn;
      private ImageView uploadImage;
      private ProgressBar uploadbar;
      private TextView discription;
    private FirebaseAuth.AuthStateListener authListener;
    private  FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,databaseReferencePost;
    StorageReference databaseReferenceImage;
    StorageTask storageTask;
    Uri img;
    Handler handler;
    private SharedPreferences setting;
    public BlankFragment2() {
        // Required empty public constructor
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_blank2, container, false);
       handler=new Handler();
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReferenceImage= FirebaseStorage.getInstance().getReference("Images");
        databaseReferencePost= FirebaseDatabase.getInstance().getReference("Posts");
        setting = getActivity().getSharedPreferences("Setting", Context.MODE_PRIVATE);
        uploadImage=view.findViewById(R.id.upload_image);
        btn=view.findViewById(R.id.photo_upload);
        uploadbar=getActivity().findViewById(R.id.upload_progressBar);
        if (uploadbar!=null){
            uploadbar.setVisibility(View.GONE);
        }
        discription=view.findViewById(R.id.discription);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img!=null){
                    if (storageTask!=null && storageTask.isInProgress()){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),"photo upload....",Toast.LENGTH_LONG);
                            }
                        });

                    }
                    else {
                        fileupload(img);
                    }
                }
                else{
                    Toast.makeText(getActivity(),"please choose photo",Toast.LENGTH_SHORT);
                }

            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    public void fileupload(Uri url){

        final StorageReference rf=databaseReferenceImage.child(System.currentTimeMillis()+"."+getExtension(url));
        storageTask=rf.putFile(url)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploadbar.setVisibility(View.VISIBLE);
                        double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        uploadbar.setProgress((int) progress);
                        Task<Uri> uri=storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return rf.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String discriptiontxt=discription.getText().toString();
                                    Post post=new Post(discriptiontxt,downloadUri.toString());
                                    String child=databaseReferencePost.push().getKey();
                                    databaseReferencePost.child(child).setValue(post);
                                    uploadbar.setVisibility(View.GONE);
                                    uploadImage.setImageResource(R.drawable.ic_menu_camera);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), "successfull", Toast.LENGTH_LONG);
                                        }
                                    });

                                } else {

                                }
                                uploadbar.setVisibility(View.GONE);
                            }
                        });}

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        uploadbar.setVisibility(View.GONE);
                        uploadImage.setImageResource(R.drawable.ic_menu_camera);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        uploadbar.setVisibility(View.VISIBLE);
                        double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        uploadbar.setProgress((int) progress);
                    }
                });


    }
    public  String getExtension(Uri url){
        ContentResolver content =getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(content.getType(url));
    }
    public void fileChooser(){
        Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==getActivity().RESULT_OK && data!=null && data.getData()!=null){
            img=data.getData();
            uploadImage.setImageURI(img);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.linearview) {
            int i=1;
            setting.edit().putInt("layout", i).apply();
            Intent intent=new Intent(getActivity(),PostFragment.class);
            startActivity(intent);
            return true;
        }
        else if (id==R.id.gridVew){
            int i=2;
            setting.edit().putInt("layout", i).apply();
            Intent intent=new Intent(getActivity(),PostFragment.class);
            startActivity(intent);
            return true;

        }
        else if (id==R.id.logout){
            firebaseAuth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

}
