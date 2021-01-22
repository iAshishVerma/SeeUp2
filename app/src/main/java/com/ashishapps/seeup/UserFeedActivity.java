package com.ashishapps.seeup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserFeedActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int SEARCH_REQUEST_DATA_WITH_THIS_CODE = 1;
    FirebaseUser user;
    TextView userGreet, userEmail;
    Button feedToProfile, gallaryButton, uploadButton;
    ImageView pickedImage;
    Uri imageUri;//This variable holds the uri of the image choosen by the user
    FirebaseAuth auth;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        userGreet = findViewById(R.id.usergreet);
        userEmail = findViewById(R.id.useremail);
        user = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();
        setupFeed();
        ////////////Firebase
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        ///////////initialzing Onclick listenres
        pickedImage = findViewById(R.id.uploadingImage);
        uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadbutton();
            }
        });
        feedToProfile = findViewById(R.id.feedtoProfileButton);
        feedToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserFeedActivity.this, profileActivity.class);
                startActivity(intent);
            }
        });

        gallaryButton = findViewById(R.id.gallarybutton);
        gallaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GallaryImagePicker();
            }
        });

    }


    private void GallaryImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    if (data.getData() != null) {
                        imageUri = data.getData();
                        pickedImage.setImageURI(imageUri);
                    }

                }

            }

        }

    }

    private void uploadbutton() {
        if (storageReference != null) {
            StorageReference ref = storageReference.child("UserProfiles/" + auth.getCurrentUser().getUid() + "/" + "userphotos/" + UUID.randomUUID().toString() + ".png");
            ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(UserFeedActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(UserFeedActivity.this,uri.toString() , Toast.LENGTH_SHORT).show();
                        uploadToDB(uri.toString());
                    }
                });



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserFeedActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void uploadToDB(String url) {
        String URL=url,caption="",tags="";
        Map<String,Object> user= new HashMap<>();
        user.put("PhotoURL",URL);
        user.put("Caption",caption);
        user.put("Tags",tags);
        //collection matlab folder
        docRef=FirebaseFirestore.getInstance().collection("UserProfiles").document(auth.getUid().toString()).collection("userphotos").document("photos");
         docRef.set(user)
    .addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 Toast.makeText(UserFeedActivity.this, "Link to database saved", Toast.LENGTH_SHORT).show();
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(UserFeedActivity.this, "Linked To Database Failed", Toast.LENGTH_SHORT).show();
             }
         });
    }


    private void setupFeed() {


        if (user == null) {
            Toast.makeText(UserFeedActivity.this, "User Feed Fetch Error", Toast.LENGTH_SHORT).show();
        }

        if (user != null) {
            String name = user.getDisplayName();
            greetUser(name);
        }

    }

    private void greetUser(String name2) {
        userGreet.setText("Hi" + name2);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }

}
