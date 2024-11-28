package com.company.firstapp.widgets;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.company.firstapp.R;

public class MainActivity extends AppCompatActivity {
    private EditText n1, n2;
    private TextView tv_res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();
    }

    public void addListenerOnButton() {
        n1 = findViewById(R.id.edt_n1);
        n2 = findViewById(R.id.edt_n2);
        Button btn = findViewById(R.id.btn_calculate);
        tv_res = findViewById(R.id.tv_res);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int a = Integer.parseInt(n1.getText().toString());
                    int b = Integer.parseInt(n2.getText().toString());
                    tv_res.setText(String.valueOf(a + b));
                } catch (NumberFormatException e) {
                    tv_res.setText("Error: Please input both numbers");
                }
            }
        });
    }
}