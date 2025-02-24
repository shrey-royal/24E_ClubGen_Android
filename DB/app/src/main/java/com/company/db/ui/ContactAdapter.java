package com.company.db.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.company.db.R;
import com.company.db.data.ContactDao;
import com.company.db.model.Contact;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private Context context;
    private ArrayList<Contact> contactList;
    private ContactDao contactDao;

    public ContactAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
        this.context = context;
        this.contactList = contacts;
        this.contactDao = new ContactDao(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        }

        Contact contact = contactList.get(position);

//        ImageView tv_profile = convertView.findViewById(R.id.profile_img);
        TextView tv_name = convertView.findViewById(R.id.nameTextView);
        TextView tv_phone = convertView.findViewById(R.id.phoneTextView);
        ImageButton btn_edit = convertView.findViewById(R.id.editButton);
        ImageButton btn_delete = convertView.findViewById(R.id.deleteButton);

        tv_name.setText(contact.getName());
        tv_phone.setText(contact.getPhone());

        btn_edit.setOnClickListener(v -> {
            Toast.makeText(context, "Coming Soon!", Toast.LENGTH_SHORT).show();
        });

        btn_delete.setOnClickListener(v -> {
            if (contactDao.deleteContact(contact.getId())) {
                contactList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Contact Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to Delete", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }


}
