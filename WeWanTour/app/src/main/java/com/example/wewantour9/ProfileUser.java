package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileUser extends AppCompatActivity {


    FirebaseAuth fAuth;
    Snackbar snack;
    ConstraintLayout profile_layout;
    Button logout_button;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        profile_layout = (ConstraintLayout) findViewById(R.id.profile_layout);
        fAuth = fAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        logout_button = (Button)findViewById(R.id.logout_button);


        if(!currentUser.isEmailVerified()){
            snack.make(profile_layout, "Email is not verified!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Send me again the email", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //verification email
                            //FirebaseUser user = fAuth.getCurrentUser();
                            currentUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ProfileUser.this, "Verification Email has been sent!", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG", "Error: Verification Mail not sent"+ e.getMessage());

                                }
                            });

                        }
                    })
                    .show();
        }


       logout_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               fAuth.signOut();
               startActivity(new Intent(getApplicationContext(), Login.class));
               finish();
           };
       });
    }
}
