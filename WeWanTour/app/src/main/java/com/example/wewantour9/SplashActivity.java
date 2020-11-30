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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.*;

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


                            /**************************/ ArrayList<Reservation> list_reservation =  new ArrayList<Reservation>();

                            boolean flag = true;
                            Log.d("SONO qui","*******FUORI DAL FOR");
                            for(DataSnapshot data: dataSnapshot.child("Agency").getChildren() ){

                        //********************************************************************************************************

                                //Log.e("SplashActivity DATA.GETVALUE:", data.getValue().getClass()+"");
                                //Object objectBuffer = data.getValue(Object.class);
                                //String jsonFromFirebase = new Gson().toJson(objectBuffer);
                                //JsonObject jsonObject = new JsonParser().parse(jsonFromFirebase).getAsJsonObject();
                                //Log.e("SplashActivity FULL_NAME:", jsonObject.get("full_name").toString());
                                //Log.e("SplashActivity EMAIL:", jsonObject.get("email").toString());
                                //Log.e("SplashActivity IMAGE:", jsonObject.get("image").toString());
                                //Log.e("SplashActivity AGENCY_NAME:", jsonObject.get("agency_name").toString());
                                //Log.e("SplashActivity TELEPHONE NUMBER:", jsonObject.get("telephone_number").toString());
                                //Log.e("SplashActivity LOCATION:", jsonObject.get("location").toString());
                                //Log.e("SplashActivity IVA_NUMBER:", jsonObject.get("iva_number").toString());
                                //Gson g = new Gson();

                                //Log.e("SplashActivity LIST_RESERVATION:", jsonObject.get("list_reservation").toString().charAt(0)+"");

                                //if((jsonObject.get("list_reservation").toString().charAt(0)+"").equals("[")){
                                //   list_reservation = g.fromJson(jsonObject.get("list_reservation").toString(), ArrayList.class);
                                //    Log.e("SplashActivity LIST_RESERVATION:", list_reservation+"");
                                //}else{
                                    //check from here all the passage with print
                                //    HashMap<String, Reservation> hashMap_reservation = g.fromJson(jsonObject.get("list_reservation").toString(), HashMap.class);
                                    //Log.e("SplashActivity HASHMAP_RESERVATIONS:", hashMap_reservation.values()+"");
                                //    for (Entry<String, Reservation> entry : hashMap_reservation.entrySet()) {
                                //        list_reservation.add(entry.getValue());
                                //        Log.e("SplashActivity RESERVATION:", list_reservation+"");

                                //    }
                                    //Log.e("SplashActivity LIST_FROM_HASMAP_RESERVATION:", list_reservation+"");
                                //}

                                //Agency agency = new Agency();
                                //Log.e("SplashActivity AGENCY CICLE:", agency.toString());
                                //Log.d("SONO qui","*******SONO PRIMA DELL IF");
                                //Log.d("SONO qui",agency.getAgency_name());

                        //*******************************************************************************************************
                                Agency agency = data.getValue(Agency.class);

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
