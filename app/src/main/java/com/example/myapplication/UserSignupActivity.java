package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserSignupActivity extends AppCompatActivity {


    EditText mEmail, mPassword, mName;
    TextView LogInPrompt;
    Button Submit;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        mName=(EditText) findViewById(R.id.Name);
        mEmail = (EditText) findViewById(R.id.Email);
        mPassword = (EditText) findViewById(R.id.Password);
        Submit = (Button) findViewById(R.id.Submit);

        mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db= FirebaseFirestore.getInstance();
        final CollectionReference userRef= FirebaseFirestore.getInstance().collection("Users");

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(UserSignupActivity.this, EventActivity.class));
            finish();
        }


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Name=mName.getText().toString().trim();
                final String Email = mEmail.getText().toString().trim();
                final String Password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(Name)) {
                    mName.setError("Name is required.");
                    return;
                }
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

                mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(UserSignupActivity.this, "Registering Your Account", Toast.LENGTH_LONG).show();

                            FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(Name)
                                    .setPhotoUri(Uri.parse("https://www.gstatic.com/mobilesdk/160503_mobilesdk/logo/2x/firebase_28dp.png"))
                                    .build();

                            fuser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(UserSignupActivity.this, "user Profile Photo Added", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });




                            //store user id
                            String UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            //store profile picture url
                            String ProfilePicture = "https://www.gstatic.com/mobilesdk/160503_mobilesdk/logo/2x/firebase_28dp.png";

                            User user=new User(Name, Email, UserId, ProfilePicture);
                            userRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(UserSignupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(UserSignupActivity.this,EventActivity.class));
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(UserSignupActivity.this, "Error! "+task.getException().toString(), Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(UserSignupActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });


            }
        });


        LogInPrompt = (TextView) findViewById(R.id.LogInPrompt);
        LogInPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSignupActivity.this, UserLoginActivity.class);
                startActivity(intent);
            }
        });








//        //go to login activity
//        Button goToSignIn = findViewById(R.id.UserLoginActivity);
//
//        goToSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//                startActivity(new Intent(getApplicationContext(), UserUserLoginActivity.class));
//            }
//        });
    }
}