package com.example.wewantour9;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import java.util.Objects;

public class GoogleIntermediate extends AppCompatActivity {

    private Button createaccount;
    private EditText email;

    private GoogleSignInButton signinbutton;

    private Toolbar toolbar;

    private GoogleSignInClient googleSignInclient;
    private String TAG = "Prova";
    private FirebaseAuth mAuth;



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
        signinbutton = findViewById(R.id.google_button);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);



        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        googleSignInclient = GoogleSignIn.getClient(this,gso);

        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }


        });







        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newemail = email.getText().toString();

                if(checkemail()) {
                    Intent intent = new Intent(GoogleIntermediate.this, TotalRegister.class);
                    intent.putExtra("Hey", newemail);
                    intent.putExtra("Google",1);
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

    private void signIn() {

        Intent signinintent = googleSignInclient.getSignInIntent();
        startActivityForResult(signinintent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handlesigninresult(task);
        }
    }

    private void handlesigninresult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount acc = task.getResult(ApiException.class);
            Toast.makeText(this, "TUTTO BENE",Toast.LENGTH_SHORT).show();

            FirebaseGoogleAuth(acc);
        }catch(ApiException e){
            Toast.makeText(this, "TUTTO MALE",Toast.LENGTH_SHORT).show();
            //FirebaseGoogleAuth(null);
        }

    }

    private void FirebaseGoogleAuth(GoogleSignInAccount go) {
        AuthCredential authcredetnial = GoogleAuthProvider.getCredential(go.getIdToken(),null);
        final String em = go.getEmail();
        mAuth.signInWithCredential(authcredetnial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Intent intent = new Intent(GoogleIntermediate.this, TotalRegister.class);
                    intent.putExtra("Hey", em);
                    intent.putExtra("Google",2);
                    startActivity(intent);
                    Toast.makeText(GoogleIntermediate.this, "TUTTO BENE DI NUOVO",Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();

                }
                else{
                    Toast.makeText(GoogleIntermediate.this, "TUTTO BENE DI NUOVO",Toast.LENGTH_SHORT).show();

                }
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