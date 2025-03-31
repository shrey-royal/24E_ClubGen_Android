package com.royal.todo;

import android.credentials.GetCredentialException;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException;
import com.google.firebase.auth.FirebaseAuth;

import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;

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

//    private final ActivityResultLauncher<Intent> signInLauncher =
//            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                    handleSignInResult(result.getData());
//                }
//            });

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
                        .setFilterByAuthorizedAccounts(true)
                        .setServerClientId(getString(R.string.gcm_defaultSenderId))
                        .build())
                .build();

        executorService.execute(() -> {
            try {
                credentialManager.getCredentialAsync(
                        this,
                        req,
                        null,
                        null,
                        new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                            @Override
                            public void onResult(GetCredentialResponse res) {
                                handleSignInResult(res);
                            }

                            @Override
                            public void onError(@NonNull GetCredentialException e) {
                                Log.e(TAG, "Sign in failed: " + e.getMessage());
                                Toast.makeText(RegisterActivity.this, "Sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                });
            } catch (Exception e) {
                Log.e(TAG, "Error occurred while getting credentials: " + e.getMessage());
                Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSignInResult(GetCredentialResponse res) {
        Credential credential = res.getCredential();

        if (credential instanceof CustomCredential) {
            if (GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(((CustomCredential) credential).getData());
            }
        }
    }


}