package com.royal.todo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "GoogleSignIn";
    private static final int RC_SIGN_IN = 9001; // Request code for Google Sign-In

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Button btnGoogleSignIn, btnGoogleSignOut;
    private TextView userName, userEmail;
    private ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Web client ID from Firebase
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize UI elements
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignOut = findViewById(R.id.btnGoogleSignOut);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userImage = findViewById(R.id.userImage);

        btnGoogleSignIn.setOnClickListener(v -> signIn());
        btnGoogleSignOut.setOnClickListener(v -> signOut());

        updateUI(mAuth.getCurrentUser());
    }

    private void signIn() {
        // Start the sign-in flow
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the intent from GoogleSignInClient
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign-In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in success
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        Toast.makeText(this, "Sign-in successful!", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign-in fails, display a message to the user
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign-out
        mGoogleSignInClient.signOut().addOnCompleteListener(this, task -> {
            updateUI(null);
            Toast.makeText(this, "Signed Out!", Toast.LENGTH_SHORT).show();
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Update UI with user details
            userName.setText(user.getDisplayName());
            userEmail.setText(user.getEmail());
            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl()).into(userImage);
            }
            btnGoogleSignIn.setVisibility(View.GONE);
            btnGoogleSignOut.setVisibility(View.VISIBLE);
        } else {
            userName.setText("User Name");
            userEmail.setText("Email");
            btnGoogleSignIn.setVisibility(View.VISIBLE);
            btnGoogleSignOut.setVisibility(View.GONE);
        }
    }
}
