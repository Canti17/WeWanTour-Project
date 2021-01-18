package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonObject;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class tour_details extends AppCompatActivity {

    private Toolbar toolbar;

    String url;
    List<String> jsonResponses = new ArrayList<>();

    @Override
    public void onBackPressed() {
        finish();
    }

    private TextView tourTitle, startDate, startTime, startPlace, detailsText, duration, length, minPeople, nowPeople, maxPeople,
                        cost, transDate, transHour, transPlace, transCost, transReservations, noTransport, transDateLabel,
                        transHourLabel, transPlaceLabel, transCostLabel, transVehicleLabel, transReservationsLabel,
                        directRegister, weatherDescriptionField, minTemperatureField, maxTemperatureField, humidityField,
                        windSpeedField, weatherNotAvailable, reviewScore, reviewNumber;
    private ImageView vehicle, mainImage, transVehicle, deleteTransport, weatherIcon, linkIcon;
    private Button selectTransport, gotToSummaryPage;
    private ConstraintLayout bookingOptionsLayout1, bookingOptionsLayout2, weatherDescriptionLayout, weatherHumidityWindLayout;
    private ProgressBar progressCircle;
    private NumberPicker numberPicker;
    private Tour selectedTour;
    private Transport selectedTransport;
    private Reservation newReservation = new Reservation();
    private int numberOfReservationForTransport = 0;

    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;

    //weather
    private String buffer="";
    private Context context = this;
    private JSONObject weatherData;
    private String sunrise="", sunset="", weatherId="", weatherDescritpion="", minTemp="", maxTemp="", humidity="", windSpeed="";
    private Boolean weatherGoesWrong = false;
    //Maps
    private ArrayList<String> arrayCoordinatesTour;
    //Web API
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details);

        url = getApplicationContext().getResources().getString(R.string.URLServer);


        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        if(currentUser != null) Log.e("tour_details CURRENT USER", currentUser.getEmail());

        mainImage = findViewById(R.id.imageViewMain);
        tourTitle = findViewById(R.id.textViewTourTitle);
        startDate = findViewById(R.id.textViewStartDate);
        startTime = findViewById(R.id.textViewStartTime);
        startPlace = findViewById(R.id.textViewStartPlace);
        startPlace.setPaintFlags(startPlace.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        detailsText = findViewById(R.id.textViewDetailsText);
        vehicle = findViewById(R.id.imageViewVehicle);
        duration = findViewById(R.id.textViewDurationValue);
        length= findViewById(R.id.textViewLengthValue);
        minPeople = findViewById(R.id.textViewMinPeople);
        nowPeople = findViewById(R.id.textViewNowPeople);
        maxPeople = findViewById(R.id.textViewMaxPeople);
        cost = findViewById(R.id.textViewCostValue);
        selectTransport = findViewById(R.id.buttonTourDetailsAddTransport);
        numberPicker = findViewById(R.id.number_picker);
        transDate = findViewById(R.id.textViewTourDetailsTransportDateValue);
        transHour = findViewById(R.id.textViewTourDetailsTransportHourValue);
        transPlace = findViewById(R.id.textViewTourDetailsTransportPlaceValue);
        transCost = findViewById(R.id.textViewTourDetailsTransportCostValue);
        transVehicle = findViewById(R.id.imageViewTourDetailsTransportVehicleValue);
        transReservations = findViewById(R.id.textViewTourDetailsTransportNumberOfReservationsValue);
        noTransport = findViewById(R.id.textViewTourDetailsNoTransport);
        transDateLabel = findViewById(R.id.textViewTourDetailsTransportDate);
        transHourLabel = findViewById(R.id.textViewTourDetailsTransportHour);
        transPlaceLabel = findViewById(R.id.textViewTourDetailsTransportPlace);
        transCostLabel = findViewById(R.id.textViewTourDetailsTransportCost);
        transVehicleLabel = findViewById(R.id.textViewTourDetailsTransportVehicle);
        transReservationsLabel = findViewById(R.id.textViewTourDetailsTransportNumberOfReservations);
        deleteTransport = findViewById(R.id.imageViewTourDetailsTransportRemove);
        gotToSummaryPage = findViewById(R.id.buttonTourDetailsGoToSummaryPage);
        directRegister = findViewById(R.id.textViewTourDetailsRegistrationDirectLink);
        bookingOptionsLayout1 = findViewById(R.id.constraintLayoutTourDetails6);
        bookingOptionsLayout2 = findViewById(R.id.constraintLayoutTourDetails3);
        //weather fields
        weatherIcon = findViewById(R.id.imageViewTourDetailsWeather);
        weatherDescriptionField = findViewById(R.id.textViewTourDetailsWeatherDescription);
        minTemperatureField = findViewById(R.id.textViewTourDetailsMinTemperature);
        maxTemperatureField = findViewById(R.id.textViewTourDetailsMaxTemperature);
        humidityField = findViewById(R.id.textViewTourDetailsHumidity);
        windSpeedField = findViewById(R.id.textViewTourDetailsWind);
        progressCircle = findViewById(R.id.progressBarTourDetailsWeather);
        weatherDescriptionLayout = findViewById(R.id.constraintLayoutTourDetailsWeatherDescription);
        weatherHumidityWindLayout = findViewById(R.id.constraintLayoutTourDetailsWeatherHumidityWind);
        weatherNotAvailable = findViewById(R.id.textViewTourDetailsWeatherNotAvailable);

        linkIcon = findViewById(R.id.imageViewTourDetailsLinkArrow);
        reviewScore = findViewById(R.id.textViewTourDetailsReviewValue);
        reviewNumber = findViewById(R.id.textViewTourDetailsReviewNumber);


        selectedTour =  (Tour) getIntent().getSerializableExtra("Tour class from HomePage");
        Log.e("tour_details TOUR SELECTED IN THE HOMEPAGE",selectedTour.toString());

        Glide.with(getApplicationContext()).load(selectedTour.getFilePath()).into(mainImage);

        tourTitle.setText(selectedTour.getName());
        startDate.setText(selectedTour.getStartDate());
        startTime.setText(selectedTour.getStartHour());
        startPlace.setText(selectedTour.getStartPlace());
        detailsText.setText(selectedTour.getDescription());
        duration.setText(String.valueOf((int)selectedTour.getDuration())+" min");
        length.setText("   " + String.valueOf(selectedTour.getTripLength()) + " Km");
        minPeople.setText(String.valueOf(selectedTour.getMinPeople()));
        nowPeople.setText(String.valueOf(selectedTour.getCurrentPeople()));
        maxPeople.setText(String.valueOf(selectedTour.getPeopleLimit()));
        double tourCost = selectedTour.getPrice();
        if((tourCost-(int)tourCost)!=0)
            cost.setText(String.valueOf(tourCost)+ " €");
        else
            cost.setText(String.valueOf((int)tourCost)+ " €");
        Drawable myDrawable;
        if(selectedTour.getVehicle().equals("bike"))
            myDrawable = getResources().getDrawable(R.drawable.bicycle);
        else
            myDrawable = getResources().getDrawable(R.drawable.walking_man);
        vehicle.setImageDrawable(myDrawable);




        selectTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tour_details.this, List_transport_for_prenotation.class);
                intent.putExtra("Tour class for transport list for prenotation", selectedTour);
                //This is used to set the initail value of the number picker in the transports list
                if(numberOfReservationForTransport == 0){
                    intent.putExtra("Number of people to filter the transports", numberPicker.getValue());
                }else{
                    intent.putExtra("Number of people to filter the transports", numberOfReservationForTransport);
                }
                startActivityForResult(intent,1);
            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        directRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GoogleIntermediate.class));
            }
        });


        if(currentUser==null){
            selectTransport.setClickable(false);
            gotToSummaryPage.setText("Login to book the tour");
            bookingOptionsLayout1.setVisibility(View.GONE);
            bookingOptionsLayout2.setVisibility(View.GONE);

            gotToSummaryPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(tour_details.this, Login.class);
                    startActivity(intent);
                }
            });
        }else{
            //Choose number widget
            numberPicker.setMax(selectedTour.getPeopleLimit()-selectedTour.getCurrentPeople());
            numberPicker.setMin(1);
            numberPicker.setUnit(1);
            numberPicker.setValue(1);
            directRegister.setVisibility(View.GONE);

            gotToSummaryPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newReservation.setTransport(selectedTransport);
                    newReservation.setTour(selectedTour);
                    newReservation.setCustomer(currentUser.getEmail());
                    newReservation.setNumberOfPeople(numberPicker.getValue());
                    newReservation.setTransportNumberOfPeople(numberOfReservationForTransport);
                    Log.e("tour_details RESERVATION CLASS TO SEND TO THE SUMMARY PAGE",newReservation.toString());
                    Intent intent = new Intent(tour_details.this, reservation_summary.class);
                    intent.putExtra("Reservation class from tour details to summary", newReservation);
                    startActivity(intent);
                }
            });

        }


        numberPicker.setValueChangedListener(new ValueChangedListener() {
                @Override
                public void valueChanged(int x, ActionEnum y) {
                    if(selectedTransport!=null){
                        int availableSeats = selectedTransport.getMaxPeople()-selectedTransport.getCurrentPeople();
                        if(availableSeats < numberPicker.getValue()){
                            new AlertDialog.Builder(tour_details.this)
                                    .setTitle("Warning")
                                    .setMessage("The maximum number of free seats available for the selected transport is "+ availableSeats)

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    /*.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation
                                        }
                                    })*/

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(getResources().getDrawable(R.drawable.error))
                                    .show();
                                    numberPicker.setValue(numberPicker.getValue()-1);
                        }
                    }
                }
        });

        //Impostazione iniziale, nessuntrasporto selezionato
        transVehicle.setVisibility(View.INVISIBLE);
        transCost.setVisibility(View.INVISIBLE);
        transPlace.setVisibility(View.INVISIBLE);
        transDate.setVisibility(View.INVISIBLE);
        transHour.setVisibility(View.INVISIBLE);
        transReservations.setVisibility(View.INVISIBLE);
        transVehicleLabel.setVisibility(View.INVISIBLE);
        transCostLabel.setVisibility(View.INVISIBLE);
        transPlaceLabel.setVisibility(View.INVISIBLE);
        transDateLabel.setVisibility(View.INVISIBLE);
        transHourLabel.setVisibility(View.INVISIBLE);
        transReservationsLabel.setVisibility(View.INVISIBLE);
        deleteTransport.setVisibility(View.INVISIBLE);
        //Weather starting impostation
        progressCircle.setVisibility(View.VISIBLE);
        weatherDescriptionLayout.setVisibility(View.GONE);
        weatherHumidityWindLayout.setVisibility(View.GONE);

        deleteTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transVehicle.setVisibility(View.INVISIBLE);
                transCost.setVisibility(View.INVISIBLE);
                transPlace.setVisibility(View.INVISIBLE);
                transDate.setVisibility(View.INVISIBLE);
                transHour.setVisibility(View.INVISIBLE);
                transReservations.setVisibility(View.INVISIBLE);
                transVehicleLabel.setVisibility(View.INVISIBLE);
                transCostLabel.setVisibility(View.INVISIBLE);
                transPlaceLabel.setVisibility(View.INVISIBLE);
                transDateLabel.setVisibility(View.INVISIBLE);
                transHourLabel.setVisibility(View.INVISIBLE);
                transReservationsLabel.setVisibility(View.INVISIBLE);
                selectTransport.setText("ADD TRANSPORT");
                noTransport.setVisibility(View.VISIBLE);
                deleteTransport.setVisibility(View.INVISIBLE);
                selectedTransport = null;
                numberOfReservationForTransport = 0;
            }
        });

        //Weather display (the buffer variable is just for the debugging)
        Thread t = new Thread(){
            public void run(){

                arrayCoordinatesTour = API_usage.getCoordinates(context, selectedTour.getStartPlace());

                if(arrayCoordinatesTour.get(0) != null){

                    weatherData = API_usage.getWeather(context, arrayCoordinatesTour, selectedTour.getStartDate());

                    buffer = arrayCoordinatesTour.toString();

                    if(weatherData != null){

                        buffer = weatherData.toString();

                        try{
                            sunrise = weatherData.getString("sunrise");
                            sunset = weatherData.getString("sunset");
                            weatherId = weatherData.getJSONArray("weather").getJSONObject(0).getString("id");
                            weatherDescritpion = weatherData.getJSONArray("weather").getJSONObject(0).getString("description");
                            weatherDescritpion = weatherDescritpion.substring(0, 1).toUpperCase() + weatherDescritpion.substring(1);
                            minTemp = weatherData.getJSONObject("temp").getString("min");
                            maxTemp = weatherData.getJSONObject("temp").getString("max");
                            humidity = weatherData.getString("humidity");
                            windSpeed = weatherData.getString("wind_speed");
                            buffer = sunrise+" // "+sunset+" // "+weatherId+" // "+weatherDescritpion+" // "+minTemp+" // "+maxTemp+" // "+humidity+" // "+windSpeed;

                            //something to execute in the main thread, only the main thread is able to modify the view
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    weatherIcon.setImageDrawable(API_usage.getIconFromWeatherCode(context, Integer.parseInt(weatherId), sunrise, sunset, selectedTour.getStartHour()));
                                    weatherDescriptionField.setText(weatherDescritpion);
                                    minTemperatureField.setText("Min: " + minTemp + " C°");
                                    maxTemperatureField.setText("Max: " + maxTemp + " C°");
                                    humidityField.setText(humidity + " %");
                                    windSpeedField.setText(windSpeed + " m/s");
                                    weatherDescriptionLayout.setVisibility(View.VISIBLE);
                                    weatherHumidityWindLayout.setVisibility(View.VISIBLE);
                                    progressCircle.setVisibility(View.GONE);

                                }
                            });

                        }catch (Exception e){
                            //Case the parse of the weather response not goes well
                            weatherGoesWrong = true;
                            buffer = e + weatherData.toString();
                        }
                    }else{
                        //Case api don't find the meteo
                        weatherGoesWrong = true;
                        buffer = "NULL weather ";
                    }
                }else{
                    //Case api don't find the coordinates
                    weatherGoesWrong = true;
                    buffer = "NULL coordinates";
                }

                if(weatherGoesWrong){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            weatherNotAvailable.setVisibility(View.VISIBLE);
                            progressCircle.setVisibility(View.GONE);
                        }
                    });
                }

                Log.e("WEATHER/GEOCODING API DEBUG", buffer);

            }
        };
        t.start();

        startPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMapsForTourDetails();
            }
        });

        linkIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMapsForTourDetails();
            }
        });


        SpannableStringBuilder ratingString = new SpannableStringBuilder("Rating:   5.0 / 5.0");
        ratingString.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ratingString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        reviewScore.setText(ratingString);

        pollingForReviews();

    }

    public void googleMapsForTourDetails(){
        if(arrayCoordinatesTour.get(0) != null) {
            Uri googleMapPointURI = Uri.parse("geo:"+arrayCoordinatesTour.get(0) + "," + arrayCoordinatesTour.get(1)+"?q=" + arrayCoordinatesTour.get(0) + "," + arrayCoordinatesTour.get(1)+"("+selectedTour.getName()+")"+"&z=1");
            Intent googleMapIntent = new Intent(Intent.ACTION_VIEW, googleMapPointURI);
            googleMapIntent.setPackage("com.google.android.apps.maps");
            Log.e("DEBUG ",googleMapPointURI.toString());
            if (googleMapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(googleMapIntent);
            }else{
                String text = "Install Google Maps or if it is already opened close it";
                Spannable centeredText = new SpannableString(text);
                centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                        0, text.length() - 1,
                        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                Toast toast = Toast.makeText(context, centeredText, Toast.LENGTH_SHORT);
                toast.show();
            }
        }else{
            String text = "Invalid location";
            Spannable centeredText = new SpannableString(text);
            centeredText.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0, text.length() - 1,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            Toast toast = Toast.makeText(context, centeredText, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void pollingForReviews(){
        //GET CALL TO REVIEW VALUE
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + "nametour=" + selectedTour.getName(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONArray jsonArray = response.getJSONArray("data");
                    //for(int i = 0; i < jsonArray.length(); i++){
                    //JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String msg = response.getString("msg");

                    Double rating = response.getDouble("rating");
                    int number = response.getInt("number");

                    SpannableStringBuilder ratingString = new SpannableStringBuilder("Rating:   " + rating + " / 5.0");
                    ratingString.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ratingString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    reviewScore.setText(ratingString);

                    reviewNumber.setText(number + "  Reviews");

                    Log.i("VOLLEY", "Ha funzionato");
                    Log.i("VOLLEY", "La risposta è:" + msg);

                    jsonResponses.add(msg);
                    // }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("VOLLEY", "Errore");
                Log.e("VOLLEY", error.toString());

                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pollingForReviews();
                    }
                }, 1000);

            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }

    //Callback function from the transport list
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {

                    selectedTransport =  (Transport) data.getSerializableExtra("Transport from list Transports for booking");
                    numberOfReservationForTransport = (int) data.getSerializableExtra("People number for transport from list Transports for booking");

                    noTransport.setVisibility(View.INVISIBLE);
                    transVehicle.setVisibility(View.VISIBLE);
                    transCost.setVisibility(View.VISIBLE);
                    transPlace.setVisibility(View.VISIBLE);
                    transDate.setVisibility(View.VISIBLE);
                    transHour.setVisibility(View.VISIBLE);
                    transReservations.setVisibility(View.VISIBLE);
                    transVehicleLabel.setVisibility(View.VISIBLE);
                    transCostLabel.setVisibility(View.VISIBLE);
                    transPlaceLabel.setVisibility(View.VISIBLE);
                    transDateLabel.setVisibility(View.VISIBLE);
                    transHourLabel.setVisibility(View.VISIBLE);
                    transReservationsLabel.setVisibility(View.VISIBLE);
                    deleteTransport.setVisibility(View.VISIBLE);

                    Drawable myDrawableTrans;
                    transDate.setText(selectedTransport.getStartDate());
                    transHour.setText(selectedTransport.getStartHour());
                    transPlace.setText(selectedTransport.getStartLocation());
                    transReservations.setText(Integer.toString(numberOfReservationForTransport));
                    double transportCost = selectedTransport.getCost();
                    if((transportCost-(int)transportCost)!=0) {
                        transCost.setText(String.valueOf(transportCost) + " €");
                    }else {
                        transCost.setText(String.valueOf((int) transportCost) + " €");
                    }
                    if(selectedTransport.getVehicle().equals("Bus")){
                        myDrawableTrans = getResources().getDrawable(R.drawable.bus);
                    }else{
                        myDrawableTrans = getResources().getDrawable(R.drawable.car);
                    }
                    transVehicle.setImageDrawable(myDrawableTrans);
                    selectTransport.setText("CHANGE TRANSPORT");
                }
                break;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_toolbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int res_id = item.getItemId();
        if (res_id == R.id.menu_info) {
            AlertDialog.Builder info = new AlertDialog.Builder(tour_details.this);
            info.setMessage("Remember: You can book a transport only for all the people in your group not only a part!");
            info.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            info.create().show();
        } else {

            finish();
        }
        return true;
    }


}


