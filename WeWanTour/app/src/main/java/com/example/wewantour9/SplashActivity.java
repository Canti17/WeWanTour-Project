package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                final FirebaseUser current_user = fAuth.getInstance().getCurrentUser();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USER");

                boolean flag = true;

                if(current_user != null){


                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String emailuser = current_user.getEmail();

                            boolean flag = true;
                            Log.d("SONO qui","*******FUORI DAL FOR");
                            for(DataSnapshot data: dataSnapshot.child("Agency").getChildren() ){
                                Agency agency = data.getValue(Agency.class);
                                Log.d("SONO qui","*******SONO PRIMA DELL IF");
                                Log.d("SONO qui",agency.getAgency_name());
                                if(emailuser.equals(agency.getEmail())){
                                    flag = false;
                                    Log.d("SONO qui","*******SONO DENTRO DELL IF");
                                    Intent intent = new Intent(getApplicationContext(), HomepageAgency.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                            }
                            if(flag) {
                                Log.d("SONO qui", "*******PARTE LA MAIN ACTIVITY");
                                Intent intent = new Intent(getApplicationContext(), Homepage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }








                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




                }
                else{

                    Intent intent = new Intent(getApplicationContext(), Homepage.class);
                    startActivity(intent);
                    finish();

                }

            }

            }, 1000);
        }
}
