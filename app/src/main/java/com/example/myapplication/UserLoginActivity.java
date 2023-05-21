package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserLoginActivity extends AppCompatActivity {

    EditText mEmail, mPassword;
    TextView SignUpPrompt;
    Button Submit;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        mEmail = (EditText) findViewById(R.id.Email);
        mPassword = (EditText) findViewById(R.id.Password);
        Submit = (Button) findViewById(R.id.Submit);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(UserLoginActivity.this, MainActivity.class));
            finish();
        }

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = mEmail.getText().toString().trim();
                String Password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(Email)) {
                    mEmail.setError("Email is required.");
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    mPassword.setError("Password is required.");
                    return;
                }
                if (Password.length() < 8) {
                    mPassword.setError("Password cannot be shorter than 8 characters.");
                }

                mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserLoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(UserLoginActivity.this,EventActivity.class));
                            finish();
                        } else {
                            Toast.makeText(UserLoginActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });




        SignUpPrompt = (TextView) findViewById(R.id.SignUpPrompt);
        SignUpPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoginActivity.this, UserSignupActivity.class);
                startActivity(intent);
            }

        });


        //go to signin activity
//        Button goToSignIn = findViewById(R.id.SignupActivity);
//
//        goToSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//                startActivity(new Intent(getApplicationContext(), UserSignupActivity.class));
//            }
//        });
    }
}