package com.example.rideshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Activity for email verification.
 *
 * @author Sukriti
 * @version 1.0
 */
public class VerifyEmailActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    Button verifyEmailButton, resendEmailButton;
    Toolbar toolbar;

    private static final String TAG = "VerifyEmailActivity";
    private static final int TOAST_Y_OFFSET = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser().reload();

        verifyEmailButton = findViewById(R.id.verifyEmailButton);
        resendEmailButton = findViewById(R.id.resendEmailButton);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // If user clicks on verified
        verifyEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.getCurrentUser().reload();

                // If email is not verified
                if (!mAuth.getCurrentUser().isEmailVerified()) {
                    Log.i(TAG, "User email not verified.");
                    Toast toast = Toast.makeText(getApplicationContext(), "Email has not been verified.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, TOAST_Y_OFFSET);
                    toast.show();
                }
                // If email is verified, go to CreateBookingActivity
                else {
                    Log.i(TAG, "Email verified.");
                    finish();
                    Intent intent = new Intent(VerifyEmailActivity.this, CreateBookingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        // If user clicks on resend email button
        resendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Resending email for verification.");
                mAuth.getCurrentUser().sendEmailVerification();
                Toast toast = Toast.makeText(getApplicationContext(), "Verification email has been sent.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, TOAST_Y_OFFSET);
                toast.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // If user click on logout, sign out and take user to log in activity
        if (item.getItemId() == R.id.logout) {
            Log.i(TAG, "Logging out.");
            mAuth.signOut();
            finish();
            Intent intent = new Intent(VerifyEmailActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return true;
    }
}

