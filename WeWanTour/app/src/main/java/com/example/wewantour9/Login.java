package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import org.w3c.dom.Text;

import java.util.Objects;

public class Login extends AppCompatActivity {

    Button login_button;
    EditText  email, password;
    FirebaseAuth fAuth;
    ProgressBar progress;
    TextView link;
    TextView forgot;

    TextInputLayout passwordbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button =  findViewById(R.id.login_button);

        email =  findViewById(R.id.email_field_login);
        password = findViewById(R.id.password_field_login);
        fAuth = FirebaseAuth.getInstance();
        progress =  findViewById(R.id.progressBar);
        link =  findViewById(R.id.link);
        forgot =  findViewById(R.id.forgotpassword);

        passwordbox = findViewById(R.id.password_text);



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



        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (loginUser(email, password)) {
                    progress.setVisibility(View.VISIBLE);
                    fAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                            Toast.makeText(Login.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();

                            Log.d("SONO qui","*******SONO DENTRO TASK SUCCESSFUL");

                            //FirebaseUser current_user = fAuth.getCurrentUser();
                            final String emailuser = email.getText().toString().trim();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("USER");

                            //PER IL MOMENTO VAI A MAIN ACTIVITY
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            //DatabaseReference referencecustomer = FirebaseDatabase.getInstance().getReference("User").child("Customer");
/*
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    Log.d("SONO qui","*******FUORI DAL FOR");
                                    for(DataSnapshot data: dataSnapshot.child("Agency").getChildren() ){
                                        Agency agency = data.getValue(Agency.class);
                                        Log.d("SONO qui","*******SONO PRIMA DELL IF");
                                        Log.d("SONO qui",agency.getAgency_name());
                                        if(emailuser.equals(agency.getEmail())){
                                            Log.d("SONO qui","*******SONO DENTRO DELL IF");
                                            startActivity(new Intent(getApplicationContext(), HomepageAgency.class));
                                            finish();
                                        }

                                        else{
                                            Log.d("SONO qui","*******PARTE LA MAIN ACTIVITY");
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            finish();
                                        }


                                    }



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });*/

                            }
                            else{
                                Toast.makeText(Login.this, "Authentication failed."+ Objects.requireNonNull(task.getException()).getMessage(),
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


            private boolean loginUser(EditText email, EditText password) {

                boolean var = true;

                if (isEmpty(email)) {
                    email.setError("Email is required!");
                    var = false;
                }

                if (Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()) {
                    /*DO NOTHING*/
                } else {
                    email.setError("Email Format is wrong!");
                    var = false;

                }


                if (isEmpty(password)) {
                    passwordbox.setPasswordVisibilityToggleEnabled(false);
                    password.setError("Password is required!");
                    var = false;
                }

                return var;
            };

    });


        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TotalRegister.class));
            }
        });



        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resettext = new EditText(view.getContext());
                AlertDialog.Builder passwordresetdialog = new AlertDialog.Builder(view.getContext());
                passwordresetdialog.setMessage("Enter your Email to reset the password");
                passwordresetdialog.setView(resettext);

                passwordresetdialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get the email and reset the password
                        String mail = resettext.getText().toString();
                        if (mail.isEmpty()) {
                                //do nothing
                        } else {
                            fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Login.this, "Done! Check your Email!", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Login.this, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    }
                });



                passwordresetdialog.create().show();
            }
        });



    };



    static boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

}
