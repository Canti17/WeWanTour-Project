package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class Activity_Registration_Agency extends AppCompatActivity {

    private Button registration_button;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    EditText full_name, email, password, password_confirmation,agency_name,telephone_number,iva_number;
    /*ImageButton image_button;*/
    CheckBox privacy_checkbox;

    CountryCodePicker ccp;



    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__registration__agency);

        registration_button = (Button) findViewById(R.id.register_button);

        full_name = (EditText) findViewById(R.id.fullname_text);
        email = (EditText) findViewById(R.id.email_text);
        password = (EditText) findViewById(R.id.password_text);
        password_confirmation = (EditText) findViewById(R.id.confirmPassword_text);
        /*image_button = (ImageButton) findViewById(R.id.imageButton);*/
        privacy_checkbox = (CheckBox) findViewById(R.id.checkBox);
        telephone_number = (EditText) findViewById(R.id.telephone_number);
        iva_number = (EditText) findViewById(R.id.iva_number);
        agency_name = (EditText) findViewById(R.id.agencyname_text);

        ccp = (CountryCodePicker) findViewById(R.id.ccp);   /*PLACING ITA/+39 AS DEFAULT PREFIX PHONE*/
        ccp.setDefaultCountryUsingNameCode("IT");
        ccp.resetToDefaultCountry();

        reference = database.getInstance().getReference("USER").child("Agency");



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

                String telephone = telephone_number.getText().toString();
                telephone = ccp.getSelectedCountryCodeWithPlus() + telephone_number.getText().toString();

                /* ALL CONTROLS*/
                if (registerUser(full_name, email, password,password_confirmation, privacy_checkbox)) {



                    Agency agency = new Agency(full_name.getText().toString(), email.getText().toString(),
                       password.getText().toString(), null, id, agency_name.getText().toString(),
                        telephone , "Rome" ,iva_number.getText().toString());

                    reference.child(String.valueOf(agency.getId())).setValue(agency);
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

                if (isEmpty(agency_name)) {
                    agency_name.setError("Agency Name is required!");
                    var = false;
                }

                if (isEmpty(telephone_number)) {
                    telephone_number.setError("Telephone number is required!");
                    var = false;
                }
                if (isEmpty(iva_number)) {
                    iva_number.setError("IVA number is required!");
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





