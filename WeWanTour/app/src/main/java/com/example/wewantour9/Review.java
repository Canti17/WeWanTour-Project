package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.media.Rating;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Review extends AppCompatActivity {

    private Toolbar toolbar;
    private Button sendReview;
    private RatingBar rating;
    private EditText text;
    private RequestQueue requestQueue;

    private FirebaseAuth fAuth;
    FirebaseUser current_user;
    private String name_tour;

    String url;


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);




        sendReview = findViewById(R.id.sendReview);
        text = findViewById(R.id.textarea);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Review");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        fAuth = FirebaseAuth.getInstance();
        current_user = fAuth.getCurrentUser();

        Bundle extras = getIntent().getExtras();
        name_tour = extras.getString("tour_key_name_for_reservation");

        rating = (RatingBar) findViewById(R.id.rating);

        url = getApplicationContext().getResources().getString(R.string.URLServer);


        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(Review.this, String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(this);


        sendReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    float ratingValue = rating.getRating();
                    String rating = String.valueOf(ratingValue);
                    String testo = text.getText().toString();

                    //SEND VALUES TO SERVER

                try {


                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("Comment", testo);

                    final String requestBody = jsonBody.toString();

                    Log.i("VOLLEY","Provo ad inviare questi dati: email="+current_user.getEmail()+", tourID="+name_tour+", rating="+rating+" e il commento="+testo);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url+"email="+current_user.getEmail()+"&nametour="+name_tour+"&rate="+rating, jsonBody, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("VOLLEY", "Ha funzionato!");
                            Log.i("VOLLEY", response.toString());

                            Toast.makeText(getApplicationContext(), "Grazie per aver recensito questo Tour!" , Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }, new Response.ErrorListener()  {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                            Log.e("VOLLEY", "Errore");
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;
                            }
                        }

                    };

                    requestQueue.add(request);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
}