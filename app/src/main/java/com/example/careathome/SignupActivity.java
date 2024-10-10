package com.example.careathome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    // Firebase Authentication
    FirebaseAuth mauth;

    // Views
    EditText firstNameEditText, lastNameEditText, addressEditText, phoneNumberEditText, emailEditText, passwordEditText;
    MaterialButton signupButton;
    TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mauth = FirebaseAuth.getInstance();

        // Initialize views
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginTextView = findViewById(R.id.loginTextView);

        // Handle signup button click
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate input
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create user object and store in database
                Pojo pojo = new Pojo();
                pojo.setFirstNameEditText(firstName);
                pojo.setLastNameEditText(lastName);
                pojo.setAddressEditText(address);
                pojo.setPhoneNumberEditText(phoneNumber);
                pojo.setEmailEditText(email);
                pojo.setPasswordEditText(password);

                DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
                String uid = mdatabase.push().getKey();
                mdatabase.child("Users").child(uid).setValue(pojo);

                // Perform Firebase sign-up
                Signup(email, password);
            }
        });

        // Handle login text click
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Sign up method
    public void Signup(String email, String password) {
        mauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Success: Get user ID
                    String uid = mauth.getCurrentUser().getUid();
                    Toast.makeText(SignupActivity.this, "User Created: " + uid, Toast.LENGTH_LONG).show();

                    // Navigate to home page
                    NavigateToHomePage();
                } else {
                    // Sign-up failed
                    Log.e("SignupActivity", "Signup failed.", task.getException());
                    Toast.makeText(SignupActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Navigate to home screen
    public void NavigateToHomePage() {
        Intent intent = new Intent(SignupActivity.this, Home_Screen.class);
        startActivity(intent);
        finish(); // Close the signup activity
    }
}
