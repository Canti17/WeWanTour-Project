package com.example.wewantour9;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

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

import java.util.ArrayList;
import java.util.Calendar;

public class add_tour extends AppCompatActivity {

    private Button btn_DatePicker, btn_HourPicker, btn_submit;
    private ImageButton btn_bike, btn_walk;
    private EditText edtxt_name, edtxt_description, edtxt_startCity,edtxt_startStreet, edtxt_startCivic, edtxt_StartDate, edtxt_StartHour, edtxt_price,
            edtxt_duration, edtxt_minPeople, edtxt_peopleLimit;
    private TextView txt_vehicle;
    private int outYear, outMonthOfYear, outDayOfMonth, outMinute, outHourOfDay;
    private Button btn_chooseImg;
    private EditText editText3;
    private String FileName;

    private double doublePrice;
    private double doubleDuration;
    private String tourName;
    private String tourDescription;
    private String startPlace;
    private String startDate;
    private String startHour;
    private int minPeople;
    private int peopleLimit;
    private String vehicle="";
    private boolean bike_pressed=false;
    private boolean walk_pressed=false;
    private Tour tour;

    private Toolbar toolbar;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // Uri indicates, where the image will be picked from
    private Uri filePath;
    // instance for firebase storage and StorageReference
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference reference_img;
    private UploadTask uploadTask;

    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;



    private ArrayList<Tour> list_tour_currentUser=new ArrayList<Tour>();

    private FirebaseDatabase database;
    private DatabaseReference db;
    private DatabaseReference db_User;
    private String new_tour_id;
    private String uriPath="";

    public String pad(int input) {
        String str = "";
        if (input >= 10) {
            str = Integer.toString(input);
        } else {
            str = "0" + Integer.toString(input);
        }
        return str;
    }

    // Select Image method
    private void ChooseImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }
    /*public String getNextId(DataSnapshot postSnapshot) {
        ArrayList<String> lastList = new ArrayList<String>();
        for (DataSnapshot postSnapshotList : postSnapshot.getChildren()) {
            lastList.add(postSnapshotList.getKey());
        }
        int id_progressive;
        if(lastList.size() != 0) {
            id_progressive = Integer.parseInt(lastList.get(lastList.size() - 1)) + 1;
        }else{
            id_progressive = 0;
        }
        return String.valueOf(id_progressive);
    }*/

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
            if(fileNamesplitted.length== fileLastPath.length()){
                FileName = fileLastPath;
            }else{
                FileName = fileNamesplitted[1];
            }



            /* I need to set reference_img beacause here I take the file name from the device,
            I will use it later when I call the putFile(filePath) function*/
            reference_img = storageReference.child("tour/"+FileName);

            editText3.setText(FileName);

        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tour);



        btn_DatePicker = findViewById(R.id.btn_startdate);
        btn_HourPicker = findViewById(R.id.btn_starthour);
        btn_submit= findViewById(R.id.btn_submit);
        btn_walk= findViewById(R.id.btn_AddTransport);
        btn_bike= findViewById(R.id.btn_AddTour);
        edtxt_description = findViewById(R.id.edittxt_description);
        edtxt_startCity = findViewById(R.id.edittxt_startcity);
        edtxt_startStreet = findViewById(R.id.edittxt_startstreet);
        edtxt_startCivic = findViewById(R.id.edittxt_startcivic);
        edtxt_name = findViewById(R.id.edittxt_name);
        edtxt_StartDate = findViewById(R.id.edittxt_startdate);
        edtxt_StartHour = findViewById(R.id.edittxt_starthour);
        edtxt_price = findViewById(R.id.edittxt_price);
        edtxt_duration = findViewById(R.id.edittxt_duration);
        edtxt_minPeople = findViewById(R.id.edittxt_minPeople);
        edtxt_peopleLimit= findViewById(R.id.edittxt_peoplelimit);
        txt_vehicle = findViewById(R.id.txt_vehicle);



        fAuth = FirebaseAuth.getInstance();

        currentUser = fAuth.getCurrentUser();

        Log.println(Log.ERROR,"222-STAMPO STAMPO STAMPO------------------------", currentUser.getEmail());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Tour");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialise views
        btn_chooseImg = findViewById(R.id.btnChoose);
        editText3=findViewById(R.id.edit_img);


        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images");

        // on pressing btnSelect SelectImage() is called
        btn_chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImage();
            }
        });


        db_User=database.getInstance().getReference("USER").child("Agency");
        db= database.getInstance().getReference("TOUR");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> lastList = new ArrayList<String>();
                for (DataSnapshot postSnapshotList : dataSnapshot.getChildren()) {
                    lastList.add(postSnapshotList.getKey());
                }
                int id_progressive;
                if(lastList.size() != 0) {
                    id_progressive = Integer.parseInt(lastList.get(lastList.size() - 1)) + 1;
                }else{
                    id_progressive = 0;
                }
                new_tour_id =  String.valueOf(id_progressive);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });




        btn_DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog date_picker = new DatePickerDialog(add_tour.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                edtxt_StartDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                outYear = year;
                                outMonthOfYear = monthOfYear;
                                outDayOfMonth = dayOfMonth;
                            }
                        }, year, month, day);
                date_picker.show();
            }
        });


        btn_HourPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar2 = Calendar.getInstance();
                int hour = calendar2.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar2.get(Calendar.MINUTE);
                // time picker dialog
                TimePickerDialog hour_picker = new TimePickerDialog(add_tour.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                edtxt_StartHour.setText(pad(sHour) + ":" + pad(sMinute));
                                outHourOfDay = sHour;
                                outMinute = sMinute;
                            }
                        }, hour, minutes, true);
                hour_picker.show();

            }
        });


        btn_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bike_pressed){
                    bike_pressed=false;
                    btn_bike.setBackgroundResource(R.drawable.imgbutton_color1_blue_version);
                }
                btn_walk.setBackgroundResource(R.drawable.modified_date_time_button);
                walk_pressed=true;
                txt_vehicle.setError(null);
            }
        });

        btn_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(walk_pressed) {
                    walk_pressed = false;
                    btn_walk.setBackgroundResource(R.drawable.imgbutton_color1_blue_version);
                }
                btn_bike.setBackgroundResource(R.drawable.modified_date_time_button);
                txt_vehicle.setError(null);
                bike_pressed=true;
            }
        });

        //used only to reset the error appeared in the data and time after the submit push with the empty field
        edtxt_StartDate.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) { edtxt_StartDate.setError(null); }
        });
        edtxt_StartHour.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) { edtxt_StartHour.setError(null); }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;
                if(edtxt_name.getText().toString().equalsIgnoreCase("")) {
                    edtxt_name.setError("please enter Tour name");//it gives user to info message)
                    check = false;
                }
                //campo modificabile ma obbligatorio
                if(edtxt_description.getText().toString().equalsIgnoreCase("")) {
                    edtxt_description.setError("please enter Tour description");//it gives user to info message)
                    check = false;
                }
                if(edtxt_startCity.getText().toString().equalsIgnoreCase("")) {
                    edtxt_startCity.setError("please enter city");//it gives user to info message)
                    check = false;
                }
                if(edtxt_startStreet.getText().toString().equalsIgnoreCase("")) {
                    edtxt_startStreet.setError("please enter street");//it gives user to info message)
                    check = false;
                }
                if(edtxt_startCivic.getText().toString().equalsIgnoreCase("")) {
                    edtxt_startCivic.setError("please enter civic");//it gives user to info message)
                    check = false;
                }
                if(edtxt_StartDate.getText().toString().equalsIgnoreCase("")) {
                    edtxt_StartDate.setError("please enter start date");//it gives user to info message
                    check = false;
                }
                if(edtxt_StartHour.getText().toString().equalsIgnoreCase("")) {
                    edtxt_StartHour.setError("please enter start hour");//it gives user to info message
                    check = false;
                }
                if(edtxt_price.getText().toString().equalsIgnoreCase("")) {
                    edtxt_price.setError("please enter price");//it gives user to info message
                    check = false;
                }
                if(edtxt_duration.getText().toString().equalsIgnoreCase("")) {
                    edtxt_duration.setError("please duration ");//it gives user to info message
                    check = false;
                }
                if(edtxt_minPeople.getText().toString().equalsIgnoreCase("")) {
                    edtxt_minPeople.setError("please enter min number of current people");//it gives user to info message
                    check = false;
                }
                if(edtxt_peopleLimit.getText().toString().equalsIgnoreCase("")) {
                    edtxt_peopleLimit.setError("please enter number of people limit");//it gives user to info message
                    check = false;
                }
                /*if(!((bike_pressed==true && walk_pressed==false) ||  (bike_pressed==false && walk_pressed==true))){
                    txt_vehicle.setError("please choose the vehicle");//it gives user to info message
                    check = false;
                }*/
                if(check){
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(outYear, outMonthOfYear, outDayOfMonth, outHourOfDay, outMinute);
                    doublePrice = Double.parseDouble(edtxt_price.getText().toString());
                    doubleDuration = Double.parseDouble(edtxt_duration.getText().toString());
                    tourName= edtxt_name.getText().toString();
                    tourDescription= edtxt_description.getText().toString();
                    startPlace= edtxt_startCity.getText().toString()+", "+edtxt_startStreet.getText().toString()+", "+edtxt_startCivic.getText().toString();
                    startDate=edtxt_StartDate.getText().toString();
                    startHour=edtxt_StartHour.getText().toString();
                    minPeople= Integer.parseInt(edtxt_minPeople.getText().toString());
                    peopleLimit= Integer.parseInt(edtxt_peopleLimit.getText().toString());

                    if(bike_pressed==true && walk_pressed==false){
                        vehicle="bike";
                    }else if(bike_pressed==false && walk_pressed==true){
                        vehicle="walk";
                    }

                    uploadFile();


                }
            }
        });
    }

    private void uploadFile(){
        final ProgressDialog progressDialog
                = new ProgressDialog(add_tour.this);

        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        // adding listeners on upload or failure of image

        uploadTask=reference_img.putFile(filePath);


        uploadTask.addOnSuccessListener(
                new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        // Image uploaded successfully
                        // Dismiss dialog
                        progressDialog.dismiss();
                        Toast.makeText(add_tour.this,
                                "Tour Uploaded!!",
                                Toast.LENGTH_SHORT).show();

                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uri.isComplete());
                        uriPath= uri.getResult().toString();

                        /* get current user to set agency*/
                        tour=new Tour(tourName,tourDescription,startPlace,startDate,startHour,doublePrice,doubleDuration,0,peopleLimit,minPeople,vehicle,null, uriPath);



                        db_User.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    final Agency current_agency= postSnapshot.getValue(Agency.class);
                                    if(current_agency.getEmail().equals(currentUser.getEmail())) {
                                        tour.setAgency(current_agency.getEmail());
                                        db.child(new_tour_id).setValue(tour);
                                        //db_User.child(postSnapshot.getKey()).child("list_tour").child(getNextId(postSnapshot.child("list_tour"))).setValue(tour);
                                        db_User.child(postSnapshot.getKey()).child("list_tour").child(new_tour_id).setValue(tour); //QUESTA RIGA VA SOSTITUITA ALLA PRECEDENTE QUANDO DECIDIAMO DI NON CANCELLARE PIU COSE A CAVOLO, SERVE AD AVERE UNA CONGRUENZA NEL DB TRA GLI ID /TOUR & /USER/Agency/list_tour WHEN THIS LINE USED DELETE THE FUNCTION "getNetId" ABOVE
                                        finish();
                                        startActivity(new Intent(add_tour.this, HomepageAgency.class));
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
                    public void onFailure(@NonNull Exception e)
                    {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast.makeText(add_tour.this, "Failed wewe " + e.getMessage(),Toast.LENGTH_SHORT).show();
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
                                progressDialog.setMessage(
                                        "Uploaded " + (int)progress + "%");
                            }
                        });




    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
}
