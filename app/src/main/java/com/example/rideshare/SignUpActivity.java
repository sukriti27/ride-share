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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Activity for sign up.
 *
 * @author Sukriti
 * @version 1.0
 */
public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    EditText emailEditText, passwordEditText, fullNameEditText, phoneNumberEditText;
    Button loginButton, signUpButton;
    ProgressBar progressBar;

    private static final String TAG = "SignUpActivity";
    private static final int TOAST_Y_OFFSET = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        progressBar = findViewById(R.id.progressBar);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);

        // If user clicks on log in button, take them to MainActivity
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }
        });

        // If user clicks on sign up button, try to register user
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    /**
     * Validate input fields and perform sign up using Firebase Auth
     */
    private void registerUser() {
        Log.i(TAG, "Attempt to sign up");

        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String fullName = fullNameEditText.getText().toString().trim();
        String phone = phoneNumberEditText.getText().toString().trim();

        // If name is empty
        if (fullName.isEmpty()) {
            Log.i(TAG, "Full name is empty.");
            Toast toast = Toast.makeText(getApplicationContext(), "Name is required.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, TOAST_Y_OFFSET);
            toast.show();
            fullNameEditText.requestFocus();
            return;
        }

        // If phone is empty
        if (phone.isEmpty()) {
            Log.i(TAG, "Phone is empty.");
            Toast toast = Toast.makeText(getApplicationContext(), "Phone number is required.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, TOAST_Y_OFFSET);
            toast.show();
            phoneNumberEditText.requestFocus();
            return;
        }

        // If phone has less than 10 digits
        if (phone.length() != 10) {
            Log.i(TAG, "Phone is invalid.");
            Toast toast = Toast.makeText(getApplicationContext(), "Phone number is not valid.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, TOAST_Y_OFFSET);
            toast.show();
            phoneNumberEditText.requestFocus();
            return;
        }

        // If email is empty
        if (email.isEmpty()) {
            Log.i(TAG, "Email is empty.");
            Toast toast = Toast.makeText(getApplicationContext(), "Email is required.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, TOAST_Y_OFFSET);
            toast.show();
            emailEditText.requestFocus();
            return;
        }

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

        // If password length is less than 6
        if (password.length() < 6) {
            Log.i(TAG, "Password is invalid.");
            Toast toast = Toast.makeText(getApplicationContext(), "Password must be at least 6 characters long.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, TOAST_Y_OFFSET);
            toast.show();
            passwordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                // If sign up is successful
                if (task.isSuccessful()) {
                    Log.i(TAG, "User sign up successful.");

                    addUserNameToUser();
                    addPhoneNumberToDatabase();
                    createUserHistory();
                    mAuth.getCurrentUser().sendEmailVerification();

                    // Go to verify email activity
                    finish();
                    Intent intent = new Intent(SignUpActivity.this, VerifyEmailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                // If sign up was not successful
                else {
                    Log.i(TAG, "User sign up not successful.");
                    // If user already exists, ask them to log in
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Log.i(TAG, "User already has an account.");
                        Toast toast = Toast.makeText(getApplicationContext(), "Account already exists. Please log in.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, TOAST_Y_OFFSET);
                        toast.show();
                    }
                    // If user does not have an account, show the exception that occurred
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, TOAST_Y_OFFSET);
                        toast.show();
                    }
                }
            }
        });
    }

    /**
     * Update user's profile to include their full name
     */
    private void addUserNameToUser() {
        Log.i(TAG, "Adding user name to profile");
        final FirebaseUser user = mAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullNameEditText.getText().toString())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "User name added to user profile");
                        }
                    }
                });
    }

    /**
     * Add user's contact information to database
     */
    private void addPhoneNumberToDatabase() {
        Log.i(TAG, "Adding user's contact information to database");
        User user = new User(mAuth.getCurrentUser().getUid(), phoneNumberEditText.getText().toString());
        CollectionReference contacts = db.collection(FirestoreCollections.CONTACTS);
        contacts.document(mAuth.getCurrentUser().getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "User's contact information added to database");
                }
            }
        });
    }

    /**
     * Create a document in the history collection to store user's trips in future
     */
    private void createUserHistory() {
        Log.i(TAG, "Creating user document in history collection");
        UserTripHistory userTripHistory = new UserTripHistory();
        userTripHistory.setUserId(mAuth.getCurrentUser().getUid());
        CollectionReference history = db.collection(FirestoreCollections.HISTORY);
        history.document(mAuth.getCurrentUser().getUid()).set(userTripHistory).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i(TAG, "User document added to history collection");
            }
        });
    }
}
