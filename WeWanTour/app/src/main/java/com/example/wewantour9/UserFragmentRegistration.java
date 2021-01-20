package com.example.wewantour9;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.common.hash.Hashing;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.BufferUnderflowException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragmentRegistration extends Fragment {

   // NestedScrollView scroll;

    public UserFragmentRegistration() {
        // Required empty public constructor
    }

    private Button registration_button;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    FirebaseAuth fAuth;
    private int value;

    EditText full_name, email, password, password_confirmation;

    TextInputLayout passwordbox;
    TextInputLayout passwordboxconfirmation;
    /*ImageButton image_button;*/
    CheckBox privacy_checkbox;
    TextView checkbox_textview;
    ProgressBar progress;

    private Boolean newIdFlagAlreadySelected = false;
    private String new_customer_id;


    @SuppressLint({"ClickableViewAccessibility", "ResourceAsColor"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_registration, container, false);

        registration_button = (Button) view.findViewById(R.id.register_button);

        full_name = (EditText) view.findViewById(R.id.fullname_field);
        email = (EditText) view.findViewById(R.id.email_field);
        password = (EditText) view.findViewById(R.id.password_field);
        password_confirmation = (EditText) view.findViewById(R.id.confirm_password_field);
        /*image_button = (ImageButton) findViewById(R.id.imageButton);*/
        privacy_checkbox = (CheckBox) view.findViewById(R.id.checkBox);
        progress = (ProgressBar)view.findViewById(R.id.progressBar);

        checkbox_textview = (TextView)view.findViewById(R.id.textViewprivacy);

        passwordbox = (TextInputLayout) view.findViewById(R.id.password_text);
        passwordboxconfirmation = (TextInputLayout) view.findViewById(R.id.confirmPassword_text);

        passwordbox.setPasswordVisibilityToggleEnabled(false);
        passwordboxconfirmation.setPasswordVisibilityToggleEnabled(false);

        Bundle bundle = getArguments();
        String fixedemail = bundle.getString("key");

        value = bundle.getInt("google");

        if(value == 2){
            String fixedname = bundle.getString("key2");
            full_name.setText(fixedname);
            full_name.setEnabled(false);
            full_name.setTextColor(R.color.blackTextColor);

            password.setText("Google Account");
            password.setEnabled(false);
            password.setTextColor(R.color.blackTextColor);

            password_confirmation.setText("Google Account");
            password_confirmation.setEnabled(false);
            password_confirmation.setTextColor(R.color.blackTextColor);
        }

        email.setText(fixedemail);
        email.setEnabled(false);
        email.setTextColor(R.color.blackTextColor);



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
                if(!newIdFlagAlreadySelected){
                    new_customer_id =  String.valueOf(id_progressive);
                    newIdFlagAlreadySelected = true;
                    Log.e("UserFragmentRegistration CUSTOMER SELECT ID FUNCTION", new_customer_id+"--"+newIdFlagAlreadySelected);
                }
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
                registration_button.setClickable(false);


                /* ALL CONTROLS*/
                if (registerUser(full_name, email, password,password_confirmation, privacy_checkbox)) {
                    progress.setVisibility(View.VISIBLE);

                    if (value == 2) {
                        Customer customer = new Customer(full_name.getText().toString().trim(), email.getText().toString().trim(),
                                 "", Integer.parseInt(new_customer_id));
                        Log.e("UserFragmentRegistration CUSTOMER CLASS BEFORE THE INSERION IN THE DATABASE VALUE == 2", customer.toString());
                        reference.child(new_customer_id).setValue(customer);
                        Toast.makeText(getActivity().getApplicationContext(), "Account Created!", Toast.LENGTH_SHORT).show();
                        fAuth.signOut();
                        startActivity(new Intent(getActivity().getApplicationContext(), Login.class));

                    } else {

                        fAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    //verification email
                                    FirebaseUser user = fAuth.getCurrentUser();

                                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {

                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //Toast.makeText(requireActivity().getApplicationContext(), "Verification Email has been sent!", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("TAG", "Error: Verification Mail not sent" + e.getMessage());

                                        }
                                    });


                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getActivity().getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
                                    Customer customer = new Customer(full_name.getText().toString().trim(), email.getText().toString().trim(), "", Integer.parseInt(new_customer_id));
                                    reference.child(new_customer_id).setValue(customer);
                                    startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getActivity().getApplicationContext(), "Authentication failed." + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    progress.setVisibility(View.GONE);
                                    registration_button.setClickable(true);
                                }
                            }
                        });
                    }
                }

                else{
                    registration_button.setClickable(true);
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

                if(value != 2) {

                    if (isEmpty(password)) {
                        passwordbox.setPasswordVisibilityToggleEnabled(false);
                        password.setError("Password is required!");
                        var = false;
                    }

                    if (isEmpty(password_confirmation)) {
                        passwordboxconfirmation.setPasswordVisibilityToggleEnabled(false);
                        password_confirmation.setError("Password Confirmation is required!");
                        var = false;

                    } else if (password.getText().toString().trim().equals(password_confirmation.getText().toString().trim())) {
                        /*DO NOTHING*/
                    } else {
                        passwordboxconfirmation.setPasswordVisibilityToggleEnabled(false);
                        password_confirmation.setError("Password Confirmation is different from the password!");
                        var = false;
                    }

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
        return view;
    }
}
