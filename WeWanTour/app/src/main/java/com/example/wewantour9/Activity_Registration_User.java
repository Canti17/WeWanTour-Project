package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_Registration_User extends AppCompatActivity {

    private Button registration_button;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    FirebaseAuth fAuth;

    EditText full_name, email, password, password_confirmation;

    TextInputLayout passwordbox;
    TextInputLayout passwordboxconfirmation;
    /*ImageButton image_button;*/
    CheckBox privacy_checkbox;
    TextView checkbox_textview;
    ProgressBar progress;

    private String new_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__registration_user);

        registration_button = (Button) findViewById(R.id.register_button);

        full_name = (EditText) findViewById(R.id.fullname_field);
        email = (EditText) findViewById(R.id.email_field);
        password = (EditText) findViewById(R.id.password_field);
        password_confirmation = (EditText) findViewById(R.id.confirm_password_field);
        /*image_button = (ImageButton) findViewById(R.id.imageButton);*/
        privacy_checkbox = (CheckBox) findViewById(R.id.checkBox);
        progress = (ProgressBar)findViewById(R.id.progressBar);

        checkbox_textview = (TextView)findViewById(R.id.textViewprivacy);

        passwordbox = (TextInputLayout) findViewById(R.id.password_text);
        passwordboxconfirmation = (TextInputLayout) findViewById(R.id.confirmPassword_text);

        passwordbox.setPasswordVisibilityToggleEnabled(false);
        passwordboxconfirmation.setPasswordVisibilityToggleEnabled(false);

        fAuth = FirebaseAuth.getInstance();
        reference = database.getInstance().getReference("USER").child("Customer");


        reference.addValueEventListener(new ValueEventListener() {
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
                new_user_id =  String.valueOf(id_progressive);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        //Handle problem checkbox
        privacy_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    checkbox_textview.setError(null);
            }
        };

        });

        //Handle problem see password
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordbox.setPasswordVisibilityToggleEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        //Handle problem see passwordconfirmation
        password_confirmation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordboxconfirmation.setPasswordVisibilityToggleEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /* ALL CONTROLS*/
                if (registerUser(full_name, email, password,password_confirmation, privacy_checkbox)) {
                    progress.setVisibility(View.VISIBLE);

                    fAuth.createUserWithEmailAndPassword(email.getText().toString().trim(),  password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //verification email
                                FirebaseUser user = fAuth.getCurrentUser();
                                assert user != null;
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {

                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Activity_Registration_User.this, "Verification Email has been sent!", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG", "Error: Verification Mail not sent"+ e.getMessage());

                                    }
                                });



                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(Activity_Registration_User.this, "User Created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Login.class));
                                Customer customer = new Customer(full_name.getText().toString(), email.getText().toString(),
                                        password.getText().toString(), null, Integer.parseInt(new_user_id));
                                reference.child(String.valueOf(customer.getId())).setValue(customer);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Activity_Registration_User.this, "Authentication failed."+ task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                progress.setVisibility(View.GONE);
                            }
                        }
                    });
                }

                else{
                  //ok

                }
            };


            private boolean registerUser(EditText full_name, EditText email, EditText password, EditText password_confirmation,
                                      CheckBox checkbox) {


                boolean var = true;

                if (isEmpty(full_name)) {
                    full_name.setError("Name is required!");
                    var = false;
                }

                if (isEmpty(email)) {
                    email.setError("Email is required!");
                   var = false;
                }

                if ( Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
                    /*DO NOTHING*/
                } else{
                    email.setError("Email Format is wrong!");
                    var = false;

                }


                if (isEmpty(password)) {
                    passwordbox.setPasswordVisibilityToggleEnabled(false);
                    password.setError("Password is required!");
                    var =  false;
                }

                if (isEmpty(password_confirmation)) {
                    passwordboxconfirmation.setPasswordVisibilityToggleEnabled(false);
                    password_confirmation.setError("Password Confirmation is required!");
                    var = false;

                } else if(password.getText().toString().trim().equals(password_confirmation.getText().toString().trim())) {
                    /*DO NOTHING*/
                } else{
                    passwordboxconfirmation.setPasswordVisibilityToggleEnabled(false);
                    password_confirmation.setError("Password Confirmation is different from the password!");
                    var =  false;
                }

                if (!checkbox.isChecked()) {
                        checkbox_textview.setError("Privacy Confirmation is required!");
                        var  =  false;
                    }


                return var;
            };

            boolean isEmpty(EditText text) {
                CharSequence str = text.getText().toString();
                return TextUtils.isEmpty(str);
            }
        });


    };

}

