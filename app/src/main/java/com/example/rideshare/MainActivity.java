package com.example.rideshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Activity for log in.
 *
 * @author Sukriti
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    EditText emailEditText, passwordEditText;
    ProgressBar progressBar;
    Button loginButton, signUpButton;

    private static final String TAG = "MainActivity";
    private static final int TOAST_Y_OFFSET = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        progressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);

        // If user clicks on sign up button, take them to SignUpActivity
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });

        // If user clicks on log in button, try to log in
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // If user is already logged in
        if (mAuth.getCurrentUser() != null) {
            finish();
            // If user has not verified their email, take them to VerifyEmailActivity
            if (!mAuth.getCurrentUser().isEmailVerified()) {
                Log.i(TAG, "User email not verified.");
                Intent intent = new Intent(MainActivity.this, VerifyEmailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            // If user has verified their email, take them to CreateBookingActivity
            else {
                Log.i(TAG, "User email verified.");
                Intent intent = new Intent(MainActivity.this, CreateBookingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    }

    /**
     * Validate email and password and perform log in using Firebase Auth
     */
    private void userLogin() {
        Log.i(TAG, "Attempt to log in");

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        // If email is empty
        if (email.isEmpty()) {
            Log.i(TAG, "Email is empty.");
            Toast toast = Toast.makeText(getApplicationContext(), "Email is required.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, TOAST_Y_OFFSET);
            toast.show();
            emailEditText.requestFocus();
            return;
        }

        // If email is not valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Log.i(TAG, "Email is invalid.");
            Toast toast = Toast.makeText(getApplicationContext(), "Enter a valid email address.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, TOAST_Y_OFFSET);
            toast.show();
            emailEditText.requestFocus();
            return;
        }

        // If password is empty
        if (password.isEmpty()) {
            Log.i(TAG, "Password is empty.");
            Toast toast = Toast.makeText(getApplicationContext(), "Password is required.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, TOAST_Y_OFFSET);
            toast.show();
            passwordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Fields validated, perform Firebase authentication
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Log.i(TAG, "User login successful.");
                    finish();

                    // If login is successful and user has not verified their email previously, go to VerifyEmailActivity
                    if (!mAuth.getCurrentUser().isEmailVerified()) {
                        Log.i(TAG, "User email not verified.");
                        Intent intent = new Intent(MainActivity.this, VerifyEmailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    // If login is successful and user has verified their email previously, go to CreateBookingActivity
                    else {
                        Log.i(TAG, "User email verified.");
                        Intent intent = new Intent(MainActivity.this, CreateBookingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
                // If login is not successful, show the exception that occurred
                else {
                    Log.i(TAG, "User login not successful.");
                    Toast toast = Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, TOAST_Y_OFFSET);
                    toast.show();
                }
            }
        });
    }
}

