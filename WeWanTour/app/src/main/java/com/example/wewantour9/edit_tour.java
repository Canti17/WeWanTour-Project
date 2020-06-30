package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class edit_tour extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;

    private DatabaseReference db_User;
    private FirebaseDatabase database;
    private String uriPath="";

    private Tour current_tour;

    private final int PICK_IMAGE_REQUEST = 22;

    private EditText edtxt_tour_name;
    private EditText edtxt_description, edtxt_startCity,edtxt_startStreet, edtxt_startCivic, edtxt_StartDate, edtxt_StartHour, edtxt_price,
            edtxt_duration, edtxt_minPeople, edtxt_peopleLimit;
    private TextView txt_vehicle;
    private List_tour_inAgency_adapter list_tour_adapter;

    private ImageButton btn_bike, btn_walk;
    private Button btn_chooseImg;
    private Button btn_submit;
    private boolean bike_pressed;
    private boolean walk_pressed;

    private StorageReference reference_img;
    private UploadTask uploadTask;
    private Uri filePath;
    private String FileName;
    private EditText edit_img;

    private String vehicle;
    private String new_dest;

    private List<Tour> list_tour;

    // Select Image method
    private void ChooseImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code if request code is PICK_IMAGE_REQUEST and resultCode is RESULT_OK then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();

            String fileLastPath= filePath.getLastPathSegment();
            String[] fileNamesplitted= fileLastPath.split("/");

            FileName = fileNamesplitted[2];

            /* I need to set reference_img beacause here I take the file name from the device,
            I will use it later when I call the putFile(filePath) function*/
            reference_img = storageReference.child("tour/"+FileName);

            edit_img.setText(FileName);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tour);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fAuth = FirebaseAuth.getInstance();

        currentUser = fAuth.getCurrentUser();

        current_tour= (Tour) getIntent().getSerializableExtra("Tour class from ListTourInAgency");
        list_tour= (List<Tour>) getIntent().getSerializableExtra("Tour list from ListTourInAgency");
        vehicle=current_tour.getVehicle();
        new_dest=current_tour.getDescription();

        db_User=database.getInstance().getReference("TOUR");


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images");

        edtxt_tour_name= findViewById(R.id.edittxt_name);
        edtxt_description = findViewById(R.id.edittxt_description);
        edtxt_startCity = findViewById(R.id.edittxt_startcity);
        edtxt_startStreet = findViewById(R.id.edittxt_startstreet);
        edtxt_startCivic = findViewById(R.id.edittxt_startcivic);
        edtxt_StartDate = findViewById(R.id.edittxt_startdate);
        edtxt_StartHour = findViewById(R.id.edittxt_starthour);
        edtxt_price = findViewById(R.id.edittxt_price);
        edtxt_duration = findViewById(R.id.edittxt_duration);
        edtxt_minPeople = findViewById(R.id.edittxt_minPeople);
        edtxt_peopleLimit= findViewById(R.id.edittxt_peoplelimit);
        edit_img=findViewById(R.id.edit_img);
        txt_vehicle = findViewById(R.id.txt_vehicle);


        //CHECK
        edtxt_tour_name.setText(current_tour.getName());
        edtxt_description.setText(current_tour.getDescription());
        //I have to split StartPlace
        String[] place= current_tour.getStartPlace().split(",");
        edtxt_startCity.setText(place[0]);
        edtxt_startStreet.setText(place[1]);
        edtxt_startCivic.setText(place[2]);
        edtxt_StartDate.setText(current_tour.getStartDate());
        edtxt_StartHour.setText(current_tour.getStartHour());
        edtxt_price.setText(Double.toString(current_tour.getPrice()));
        edtxt_duration.setText(Double.toString(current_tour.getDuration()));
        edtxt_minPeople.setText(Integer.toString(current_tour.getMinPeople()));
        edtxt_peopleLimit.setText(Integer.toString(current_tour.getMinPeople()));


        filePath=Uri.parse(current_tour.getFilePath());
        Log.d("Ooooooooo",filePath.toString());
        String fileLastPath= filePath.getLastPathSegment();
        String[] fileNamesplitted= fileLastPath.split("/");
        FileName = fileNamesplitted[2];
        edit_img.setText(FileName);
        reference_img = storageReference.child("tour/"+FileName);


        btn_walk= findViewById(R.id.btn_AddTransport);
        btn_bike= findViewById(R.id.btn_AddTour);
        btn_chooseImg = findViewById(R.id.btnChoose);
        btn_submit=findViewById(R.id.btn_submit);
        edit_img=findViewById(R.id.edit_img);






        if(current_tour.getVehicle().equals("bike")){
            btn_bike.setBackgroundResource(R.drawable.imgbutton_color2);
            bike_pressed=true;
            vehicle="bike";

        }else if(current_tour.getVehicle().equals("walk")){
            btn_walk.setBackgroundResource(R.drawable.imgbutton_color2);
            walk_pressed=true;
            vehicle="walk";

        }
            btn_walk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bike_pressed) {
                        bike_pressed = false;
                        btn_bike.setBackgroundResource(R.drawable.imgbutton_color1);
                    }
                    btn_walk.setBackgroundResource(R.drawable.imgbutton_color2);
                    walk_pressed = true;
                    txt_vehicle.setError(null);
                    vehicle="walk";
                }
            });

            btn_bike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (walk_pressed) {
                        walk_pressed = false;
                        btn_walk.setBackgroundResource(R.drawable.imgbutton_color1);
                    }
                    btn_bike.setBackgroundResource(R.drawable.imgbutton_color2);
                    txt_vehicle.setError(null);
                    bike_pressed = true;
                    vehicle="bike";
                }
            });




        edtxt_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new_dest=edtxt_description.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



        edit_img.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                uploadImage();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        btn_chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
                Toast.makeText(edit_tour.this, "Tour Uploaded!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(edit_tour.this, myReservation.class));
                finish();
            }
        });


    }




    private void uploadFile(){


        // Toast.makeText(edit_tour.this, "Tour Uploaded!!", Toast.LENGTH_SHORT).show();



        db_User.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    if (current_tour.equals(postSnapshot.getValue(Tour.class))) {
                                        db_User.child(postSnapshot.getKey()).child("vehicle").setValue(vehicle);
                                        db_User.child(postSnapshot.getKey()).child("description").setValue(new_dest);
                                    }
                                }
                                //Toast.makeText(edit_tour.this, "Tour Uploaded!!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }



                        });

    }












    private void uploadImage(){


       // Toast.makeText(edit_tour.this, "Tour Uploaded!!", Toast.LENGTH_SHORT).show();


        uploadTask = reference_img.putFile(filePath);
        uploadTask.addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Image uploaded successfully
                            // Dismiss dialog


                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            uriPath = uri.getResult().toString();

                            db_User.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        if (current_tour.equals(postSnapshot.getValue(Tour.class))) {
                                            db_User.child(postSnapshot.getKey()).child("filePath").setValue(uriPath);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded

                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                }
                            });


    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }


}