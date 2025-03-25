package com.royal.todo;

import android.credentials.GetCredentialRequest;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.CredentialManager;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
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
                req.addCredentialOption(new GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(getString(R.string.gcm_defaultSenderId))
                        .build())
                .build();
    }


}