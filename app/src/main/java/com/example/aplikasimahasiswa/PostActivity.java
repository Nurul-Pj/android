package com.example.aplikasimahasiswa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

import com.theartofdev.edmodo.cropper.CropImage;

public class PostActivity extends AppCompatActivity {

    String myUrl = "";
    Uri imageUri;

    ImageView fotoPost;
    EditText namaPost, npmPost, emailPost, jurusanPost ;
    ImageButton postBtn;

    StorageReference storageReference;
    StorageTask uploadTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        fotoPost = findViewById(R.id.fotoPost);
        namaPost = findViewById(R.id.namaPost);
        npmPost = findViewById(R.id.npmPost);
        emailPost = findViewById(R.id.emailPost);
        jurusanPost = findViewById(R.id.jurusanPost);
        postBtn = findViewById(R.id.postBtn);

        storageReference = FirebaseStorage.getInstance().getReference("dataMahasiswa");

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (namaPost.getText().toString().isEmpty() && npmPost.getText().toString().isEmpty() && jurusanPost.getText().toString().isEmpty()){
                    Toast.makeText(PostActivity.this,"Lengkapi data diri diatas",Toast.LENGTH_SHORT).show();
                }else{
                    uploadGambar();
                }
            }
        });

        CropImage.activity()
                .setAspectRatio(1,1)
                .start(PostActivity.this);

    }


    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadGambar(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Posting");
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference filereference = storageReference.child(System.currentTimeMillis()
                    + "," + getFileExtension(imageUri));

            uploadTask = filereference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUrl = downloadUri.toString();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("dataMahasiswa");

                        String postid = reference.push().getKey();
                        int x = 0;

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postid",postid);
                        hashMap.put("nama",namaPost.getText().toString());
                        hashMap.put("npm",npmPost.getText().toString());
                        hashMap.put("email",emailPost.getText().toString());
                        hashMap.put("jurusan",jurusanPost.getText().toString());
                        hashMap.put("image",myUrl);
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        reference.child(postid).setValue(hashMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(PostActivity.this, ListActivity.class));
                        finish();
                    }else{
                        Toast.makeText(PostActivity.this,"Failed",Toast.LENGTH_SHORT).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            Toast.makeText(PostActivity.this,"No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK )
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            fotoPost.setImageURI(imageUri);
        }else{
            Toast.makeText(PostActivity.this,"Terjadi Kesalahan!",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this, ListActivity.class));
            finish();
        }
    }

}

