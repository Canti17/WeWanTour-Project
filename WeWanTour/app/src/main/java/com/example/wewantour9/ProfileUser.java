package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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

import java.net.URI;
import java.util.Objects;

public class ProfileUser extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 22;
    FirebaseAuth fAuth;
    FirebaseUser currentUser;
    Snackbar snack;
    DrawerLayout drawer_layout;
    private UploadTask uploadTask;
    private String uriPath = "";
    private int value;

    private String FileName;
    private Uri filePath;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference reference_img;

    private Toolbar toolbar;

    private TextView fullname, agencyname, phone, ivanumber, email,password;
    private TextView agencyedit,ivaedit, phonedit;
    private ImageView image;

    private Button button;

    ImageView edit;
    private DatabaseReference db_User;



    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        drawer_layout =  findViewById(R.id.drawer);

        email = findViewById(R.id.email);
        agencyname = findViewById(R.id.agencyname);
        fullname = findViewById(R.id.fullname);
        phone = findViewById(R.id.phone);
        ivanumber = findViewById(R.id.iva_number);
        password = findViewById(R.id.password);

        edit = findViewById(R.id.edit);

        button = findViewById(R.id.btn_profile);

        image = findViewById(R.id.image);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images");

        phonedit = findViewById(R.id.phoneedit);
        ivaedit = findViewById(R.id.ivaedit);
        agencyedit = findViewById(R.id.agencyedit);


        Intent intent = getIntent();
        value = intent.getIntExtra("Ueila", 0);
        Log.d("VALUE", Integer.toString(value));

        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();

        if(value == 0) {
            phonedit.setVisibility(View.GONE);
            ivaedit.setVisibility(View.GONE);
            agencyedit.setVisibility(View.GONE);
            ivanumber.setVisibility(View.GONE);
            phone.setVisibility(View.GONE);
            agencyname.setVisibility(View.GONE);

            db_User = FirebaseDatabase.getInstance().getReference("USER");

            DatabaseReference db = FirebaseDatabase.getInstance().getReference("USER").child("Customer");
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String emailuser = currentUser.getEmail();
                    for(DataSnapshot data: dataSnapshot.getChildren() ) {
                        Customer customer = data.getValue(Customer.class);
                        if (emailuser.equals(customer.getEmail())) {
                            fullname.setText(customer.getFull_name());
                            email.setText(customer.getEmail());
                            String aster = "";
                            for(int i= 0; i<5;i++){
                                aster+="*";
                            }
                            password.setText(aster);
                            if(!customer.getImage().equals("")){
                                Glide.with(getApplicationContext()).load(customer.getImage()).into(image);

                            }
                            break;

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        else{
            DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("USER").child("Agency");

            db2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String emailuser = currentUser.getEmail();
                    for(DataSnapshot data: dataSnapshot.getChildren() ) {
                        Agency agency = data.getValue(Agency.class);
                        if (emailuser.equals(agency.getEmail())) {
                            fullname.setText(agency.getFull_name());
                            email.setText(agency.getEmail());
                            phone.setText(agency.getTelephone_number());
                            ivanumber.setText(agency.getIva_number());
                            agencyname.setText(agency.getAgency_name());
                            String aster = "";
                            for(int i= 0; i<5;i++){
                                aster+="*";
                            }
                            password.setText(aster);
                            if(agency.getImage().equals("")){
                                Glide.with(getApplicationContext()).load(R.drawable.iconuser).into(image);
                            }
                            else{
                                Glide.with(getApplicationContext()).load(agency.getImage()).into(image);


                            }

                            break;

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseImage();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Glide.with(getApplicationContext()).load(R.drawable.logopng).into(image);
                ChooseImage();
            }
        });



        if(!currentUser.isEmailVerified()){
            snack.make(drawer_layout, "Email is not verified!", Snackbar.LENGTH_INDEFINITE)
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




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(value == 0){
                    startActivity(new Intent(getApplicationContext(), My_reservation_agency.class));
                }
                else{
                    startActivity(new Intent(getApplicationContext(), My_reservation_customer.class));
                }


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }

    private void ChooseImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code if request code is PICK_IMAGE_REQUEST and resultCode is RESULT_OK then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            Glide.with(getApplicationContext()).load(filePath).into(image);

            String fileLastPath= filePath.getLastPathSegment();
            String[] fileNamesplitted= fileLastPath.split("/");

            //FileName = fileNamesplitted[2];
            //Log.d("FileName", FileName);

            /* I need to set reference_img beacause here I take the file name from the device,
            I will use it later when I call the putFile(filePath) function*/
            reference_img = storageReference.child("Agency/"+currentUser.getEmail());


            uploadFile();

        }
    }




    private void uploadFile(){

        // adding listeners on upload or failure of image

        uploadTask=reference_img.putFile(filePath);

        uploadTask.addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        // Image uploaded successfully
                        // Dismiss dialog
                        Toast.makeText(getApplicationContext(),
                                "Image Saved!",
                                Toast.LENGTH_SHORT).show();

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        uriPath= uri.getResult().toString();



                        if(value == 0){
                            //CUSTOMEEEER
                            db_User.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String emailuser = currentUser.getEmail();
                                    for(DataSnapshot data: dataSnapshot.child("Customer").getChildren() ) {
                                        Customer customer = data.getValue(Customer.class);
                                        if (emailuser.equals(customer.getEmail())) {

                                            db_User.child("Customer").child(data.getKey()).child("image").setValue(uriPath);
                                            break;

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                        else{
                            //AGENCYYYYYY

                            db_User.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String emailuser = currentUser.getEmail();
                                    for(DataSnapshot data: dataSnapshot.child("Agency").getChildren() ) {
                                        Agency agency = data.getValue(Agency.class);
                                        if (emailuser.equals(agency.getEmail())) {

                                            db_User.child("Agency").child(data.getKey()).child("image").setValue(uriPath);
                                            break;

                                        }
                                        }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }

                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {

                        // Error, Image not uploaded
                        Toast.makeText(getApplicationContext(), "Failed wewe " + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                })


                .addOnProgressListener(
                        new OnProgressListener<UploadTask.TaskSnapshot>() {

                            // Progress Listener for loading
                            // percentage on the dialog box
                            @Override
                            public void onProgress(
                                    UploadTask.TaskSnapshot taskSnapshot)
                            {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());

                            }
                        });


    }



}
