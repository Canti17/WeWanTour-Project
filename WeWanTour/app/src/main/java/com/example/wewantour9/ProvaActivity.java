package com.example.wewantour9;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ProvaActivity extends AppCompatActivity {

    EditText edit;
    TextView txtview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prova);


        edit = (EditText) findViewById(R.id.prova);
        txtview = (TextView) findViewById(R.id.textview_name);

        // Mobile number
        edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            txtview.setVisibility(View.VISIBLE);

                        }
                    }, 80);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (edit.getText().length() > 0)
                        txtview.setVisibility(View.VISIBLE);
                    else
                        txtview.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}