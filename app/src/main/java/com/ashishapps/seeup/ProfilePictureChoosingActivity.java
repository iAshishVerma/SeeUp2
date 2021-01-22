package com.ashishapps.seeup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfilePictureChoosingActivity extends AppCompatActivity {
String name,password,email;
Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture_choosing);
        name=getIntent().getStringExtra("name");
        password=getIntent().getStringExtra("password");
        email=  getIntent().getStringExtra("email");
        UpdateUserInfo(name,email,password);
        ////////initialization buttons
        button=findViewById(R.id.profilepicturechoosingNextButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeed();
            }
        });
    }

    private void showFeed() {
     Intent intent=new Intent(ProfilePictureChoosingActivity.this,UserFeedActivity.class);
     startActivity(intent
     );
    }

    private void UpdateUserInfo(String namep,String emailp,String passwordp){
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            UserProfileChangeRequest profileUpdate= new UserProfileChangeRequest.Builder().setDisplayName(namep).build();
            user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ProfilePictureChoosingActivity.this,"Profile Updated Successfully",Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }


    }

}