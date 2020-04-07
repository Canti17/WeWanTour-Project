package com.example.wewantour9;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Button send;
    private Button butt;


    private FirebaseDatabase database;
    private DatabaseReference ref;

    EditText name, description, n_clients;
    Tour tour1;
    Prova prova1;
    int id=0;




    ArrayList<Tour> lista = new ArrayList<Tour>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        send= (Button) findViewById(R.id.button);
        butt= (Button) findViewById(R.id.butt);


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
        Button btn2 = (Button)findViewById(R.id.add_transport);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, add_transport.class));
            }
        });



        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, Prova.class));


            }
        });

    }
}
