package com.ashishapps.seeup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SineUpActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    EditText emailet2, passwordet2, nameet2;
    Button sineupgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sine_up);

        auth = FirebaseAuth.getInstance();
        nameet2 = findViewById(R.id.editTextTextPersonName);

        emailet2 = findViewById(R.id.editTextTextEmailAddress);

        passwordet2 = findViewById(R.id.editTextTextPassword);

        sineupgo = findViewById(R.id.sineupgo);
        sineupgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SineUpProcess();
            }
        });

    }

    private void SineUpProcess() {
        String name= nameet2.getText().toString();
        String email=emailet2.getText().toString();
        String password = passwordet2.getText().toString();

         if(!name.equals("")||email.contains("@")||email.equals("")||password.equals("")){

             auth.createUserWithEmailAndPassword(email, password)
                     .addOnCompleteListener(SineUpActivity.this, new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             if (task.isSuccessful()) {
                                 //User is now Signed up and signed in now Great User With Hi Ashish
                                 Log.d("Log", "Sine Up with email is successful");
                                 //   FirebaseUser user=auth.getCurrentUser();
                                 // updateUI(user);
                                 //Open ProfilePictureChoosing Activity
                                 Intent intent = new Intent(SineUpActivity.this, ProfilePictureChoosingActivity.class);
                                 intent.putExtra("name",name);
                                 intent.putExtra("email",email);
                                 intent.putExtra("password",password);
                                 startActivity(intent);


                             } else {
                                 //If the sineup fails display user some message
                                 Log.w("Log", "sineInWithEmail:failure", task.getException());
                                 Toast.makeText(SineUpActivity.this, "Authentication Failed! Please try again", Toast.LENGTH_SHORT).show();
                                 // updateUI(null);

                             }
                         }
                     });

         }else {
             Toast.makeText(SineUpActivity.this,"Please enter a valid email or password",Toast.LENGTH_SHORT).show();
         }



    }
}