package com.example.etta;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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


public class Home extends Fragment {
    private FirebaseAuth.AuthStateListener authListener;
    private  FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference,databaseReferencePost;
    StorageReference databaseReferenceImage;
    StorageTask storageTask;
    private Button btn;
    private EditText discription;
    private ProgressBar uploadbar;
    private ImageView uploadImage;
    Uri img;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReferenceImage= FirebaseStorage.getInstance().getReference("Images");
        databaseReferencePost= FirebaseDatabase.getInstance().getReference("Posts");
        uploadImage=getActivity().findViewById(R.id.upload_image);
        btn=getActivity().findViewById(R.id.photo_upload);
        uploadbar=getActivity().findViewById(R.id.upload_progressBar);
        if (uploadbar!=null){
            uploadbar.setVisibility(View.GONE);
        }
        discription=getActivity().findViewById(R.id.discription);

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
                        Toast.makeText(getActivity(),"photo upload....",Toast.LENGTH_LONG);
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

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

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
}
