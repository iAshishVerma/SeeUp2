package com.ashishapps.seeup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;//used for checking if the user is logged in or not
    TextView loggedInStatus;
    Button sineupbutton, loginButton, logingo;
    EditText emailet, passwordet, name;
    //  String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        ////Button initializations
        sineupbutton = findViewById(R.id.SineUpButton);

        sineupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SineUpProcess();
                Intent intent = new Intent(MainActivity.this, SineUpActivity.class);
                startActivity(intent);
            }
        });


        logingo = findViewById(R.id.logingo);
        logingo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Logingo();
            }

        });

        ////////////////////////////////////////////////////Edit Text iniatilization
        emailet = findViewById(R.id.editTextTextEmailAddress);
        passwordet = findViewById(R.id.editTextTextPassword);


    }

    @Override
    protected void onStart() {
        super.onStart();  //if(auth!=null)
        if( auth.getCurrentUser()!=null){
            Intent intent= new Intent(MainActivity.this,UserFeedActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(MainActivity.this, "Please Log in or Sine Up", Toast.LENGTH_SHORT).show();
        }
    }


    private void Logingo() {
        String email = emailet.getText().toString();
        String password = passwordet.getText().toString();

        if (!email.contains("@")) {
            Toast.makeText(this, "Please Enter valid email address", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please Enter a valid email address", Toast.LENGTH_SHORT).show();
        } else {
            ///
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                Intent intent = new Intent(MainActivity.this, UserFeedActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
            ////

        }

    }


//    @Override
//    public void onStart(){
//        super.onStart();
//        FirebaseUser currentUser=auth.getCurrentUser();
//
//         //   Toast.makeText(this,"Please Log in or Sine up",Toast.LENGTH_SHORT)
//            updateUI(currentUser);
//
//
//    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            //loggedInStatus.setText("You are not logged in Please Log in or Sine Up to Continue");
            //loginButton.setVisibility(View.VISIBLE);
            sineupbutton.setVisibility(View.VISIBLE);
        } else {
            //Get user information and welcome user
            String name = currentUser.getDisplayName();
            String email = currentUser.getEmail();
            Uri photoUrl = currentUser.getPhotoUrl();
            boolean emailVerified = currentUser.isEmailVerified();

        }
    }
}