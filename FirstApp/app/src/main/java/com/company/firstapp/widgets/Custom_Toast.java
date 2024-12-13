package com.company.firstapp.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.firstapp.R;

public class Custom_Toast {
    public static void showCustomToast(Context context, String message, int iconResId) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView textView = layout.findViewById(R.id.toast_message);
        textView.setText(message);

        ImageView imageView = layout.findViewById(R.id.toast_icon);
        imageView.setImageResource(iconResId);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}