package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_Registration_User extends AppCompatActivity {

    private Button registration_button;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    EditText full_name, email, password, password_confirmation;
    /*ImageButton image_button;*/
    CheckBox privacy_checkbox;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__registration_user);

        registration_button = (Button) findViewById(R.id.register_button);

        full_name = (EditText) findViewById(R.id.fullname_text);
        email = (EditText) findViewById(R.id.email_text);
        password = (EditText) findViewById(R.id.password_text);
        password_confirmation = (EditText) findViewById(R.id.confirmPassword_text);
        /*image_button = (ImageButton) findViewById(R.id.imageButton);*/
        privacy_checkbox = (CheckBox) findViewById(R.id.checkBox);


        reference = database.getInstance().getReference("USER").child("Customer");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    id= (int) dataSnapshot.getChildrenCount();
                }else{
                    ///
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        registration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* CONTROLLI PASSWORD E PRIVACY*/
                if (registerUser(full_name, email, password,password_confirmation, privacy_checkbox)) {


                    Customer customer = new Customer(full_name.getText().toString(), email.getText().toString(),
                       password.getText().toString(), null, id);
                    reference.child(String.valueOf(customer.getId())).setValue(customer);
                }

                else{
                    /*do nothing */
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

                if ( Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
                    /*DO NOTHING*/
                } else{
                    email.setError("Email Format is wrong!");
                    var = false;

                }


                if (isEmpty(password)) {
                    password.setError("Password is required!");
                    var = false;
                }

                if (isEmpty(password_confirmation)) {
                    password_confirmation.setError("Password Confirmation is required!");
                    var = false;

                } else if(password.getText().toString().equals(password_confirmation.getText().toString())) {
                    /*DO NOTHING*/
                } else{
                    password_confirmation.setError("Password Confirmation is different from the password!");
                    var = false;
                }

                if (checkbox.isChecked()) {
                    /*ok*/
                } else{
                        checkbox.setError("Privacy Confirmation is required!");
                        var = false;
                    }


                return var;
            };

            boolean isEmpty(EditText text) {
                CharSequence str = text.getText().toString();
                return TextUtils.isEmpty(str);
            }
        });


    }

    ;

}












