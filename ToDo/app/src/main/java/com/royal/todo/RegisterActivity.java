package com.royal.todo;

import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "GoogleSignIn";
    private FirebaseAuth mAuth;
    private CredentialManager credentialManager;
    private ExecutorService executorService;
    private Button btnGoogleSignIn, btnGoogleSignOut;
    private TextView userName, userEmail;
    private ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        credentialManager = CredentialManager.create(this);
        executorService = Executors.newSingleThreadExecutor();

        // Initialize UI Elements
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
        GetCredentialRequest req = new GetCredentialRequest.Builder()
                .addCredentialOption(new GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .build())
                .build();

        credentialManager.getCredentialAsync(
                this, req, null, executorService,
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                    @Override
                    public void onResult(GetCredentialResponse res) {
                        Log.d("TAG", "res");
                        handleSignInResult(res);
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        Log.e(TAG, "Sign in failed: " + e.getMessage());
                    }
                });
    }

    private void handleSignInResult(GetCredentialResponse res) {
        Credential credential = res.getCredential();

        if (credential instanceof CustomCredential) {
            if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(((CustomCredential) credential).getData());

                String idToken = googleIdTokenCredential.getIdToken();
                Log.d("TOKEN", idToken);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
                mAuth.signInWithCredential(authCredential)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Sign-in successful!", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e(TAG, "Sign-in failed!");
                            }
                        });
            }
        }
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
        Toast.makeText(this, "Signed Out!", Toast.LENGTH_SHORT).show();
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            userName.setText(user.getDisplayName());
            userEmail.setText(user.getEmail());
            Glide.with(this).load(user.getPhotoUrl()).into(userImage);
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