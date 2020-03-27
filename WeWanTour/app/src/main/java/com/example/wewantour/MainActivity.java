package com.example.wewantour;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private Button send;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    EditText name, description, n_clients;
    Tour tour1;
    int id=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        send= (Button) findViewById(R.id.button);

        name=findViewById(R.id.nameTour);
        description=findViewById(R.id.description);
        n_clients=findViewById(R.id.numb_clients);


        ref= database.getInstance().getReference("TOUR");

// ...

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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tour1= new Tour(name.getText().toString(),description.getText().toString(),n_clients.getText().toString());
                ref.child(String.valueOf(id)).setValue(tour1);
            }
        });







    }
}
