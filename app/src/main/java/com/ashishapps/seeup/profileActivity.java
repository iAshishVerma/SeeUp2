package com.ashishapps.seeup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class profileActivity extends AppCompatActivity {
Button signout;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        auth=FirebaseAuth.getInstance();
        signout=findViewById(R.id.signoutbutton);

    signout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            signoutuser();
        }
    });
    }


    private void signoutuser() {
        if(auth!=null){

            auth.signOut();
            if(auth.getCurrentUser()==null){
                Toast.makeText(profileActivity.this,"Log Out Successful",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(profileActivity.this,MainActivity.class));
                //yadi following na pyog kia gaya to log out karne ke baad main activity khulti hai aur fir "Back" press karne
                //par fir ke profile acitivity khul jati hai
                finishAffinity();

            }



        }
    }
    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }
}