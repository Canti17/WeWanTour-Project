package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private Button login_button;
    EditText  email, password;
    FirebaseAuth fAuth;
    ProgressBar progress;
    TextView link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button = (Button) findViewById(R.id.login_button);

        email = (EditText) findViewById(R.id.email_field_login);
        password = (EditText)findViewById(R.id.password_field_login);
        fAuth = FirebaseAuth.getInstance();
        progress = (ProgressBar) findViewById(R.id.progressBar);
        link = (TextView) findViewById(R.id.link);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.setVisibility(View.VISIBLE);

                if (loginUser(email, password)) {
                    fAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                            else{
                                Toast.makeText(Login.this, "Authentication failed."+ task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                progress.setVisibility(View.GONE);
                            }

                        }
                    });
            }

        };


            private boolean loginUser(EditText email, EditText password) {

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


                if (isEmpty(password)) {
                    password.setError("Password is required!");
                    return false;
                }

                return true;
            };

            boolean isEmpty(EditText text) {
                CharSequence str = text.getText().toString();
                return TextUtils.isEmpty(str);
            }


    });


        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Activity_Registration_User.class));
            }
        });

    };




}
