package com.company.firstapp.intentss;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.company.firstapp.R;

public class RedirectToAppActivity extends AppCompatActivity {
    private Button openFileButton;

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri fileUri = result.getData().getData();

                    if(fileUri != null) {
                        String mimeType = getMimeType(fileUri);

                        Log.d("RedirectToAppActivity", "File URI: " + fileUri);
                        Log.d("RedirectToAppActivity", "Mime Type: " + mimeType);

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(fileUri, mimeType);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        if(intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "No app found to open this file type", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect_to_app);

        openFileButton = findViewById(R.id.openFileButton);

        openFileButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            filePickerLauncher.launch(intent);
        });
    }

    private String getMimeType(Uri uri) {
        String mimeType = null;

        if (uri.getScheme() != null && uri.getScheme().equals("content")) {
            mimeType = getContentResolver().getType(uri);
        } else if (uri.getScheme() != null && uri.getScheme().equals("file")) {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        }

        return mimeType != null ? mimeType : "*/*";
    }

}