package com.example.wewantour9;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        name=findViewById(R.id.nameTour);
        description=findViewById(R.id.description);
        n_clients=findViewById(R.id.numb_clients);


        ref= database.getInstance().getReference("TOUR");



        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    id= (int) dataSnapshot.getChildrenCount();
                }else{
                    ///
                }
            }

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddTour.class));
            }
        });
*/

        Button add_tour =(Button)findViewById(R.id.add_tour);
        add_tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, add_tour.class));
            }
        });


        Button btn_add_transport = (Button)findViewById(R.id.add_transport);
        btn_add_transport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, add_transport.class));
            }
        });


        Button btn_registration_user= (Button)findViewById(R.id.registration_user);
        btn_registration_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Activity_Registration_User.class));
            }
        });

        Button btn_registration_agency= (Button)findViewById(R.id.registration_agency);
        btn_registration_agency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Activity_Registration_Agency.class));
            }
        });

    }
}
