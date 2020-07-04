package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class GoogleIntermediate extends AppCompatActivity {

    private Button createaccount;
    private EditText email;

    private Toolbar toolbar;



    @Override
    public void onBackPressed() {
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_intermediate);


        createaccount = findViewById(R.id.newaccountbutton);
        email = findViewById(R.id.email_field_new);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);








        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newemail = email.getText().toString();

                if(checkemail()) {
                    Intent intent = new Intent(GoogleIntermediate.this, TotalRegister.class);
                    intent.putExtra("Hey", newemail);
                    startActivity(intent);

                }
                else{
                    //nothing??
                }


            }

            private boolean checkemail() {

                boolean var = true;

                if (isEmpty(email)) {
                    email.setError("Email is required!");
                    return false;
                }

                if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                    /*DO NOTHING*/
                } else {
                    email.setError("Email Format is wrong!");
                    return false;

                }

                return true;
            }
        });




    }

    static boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
}