package intentss;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.company.firstapp.R;

public class IntentAcitvity_0 extends AppCompatActivity {
    Button btn;
    EditText edt_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_acitvity0);

        btn = findViewById(R.id.btn_submit);
        edt_text = findViewById(R.id.edt_text);


        btn.setOnClickListener(view -> {
            String url = edt_text.getText().toString();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });
    }
}