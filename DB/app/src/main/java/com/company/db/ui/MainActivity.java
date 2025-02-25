package com.company.db.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.company.db.R;
import com.company.db.data.ContactDao;
import com.company.db.model.Contact;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText searchEditText, nameEditText, phoneEditText;
    private Button addButton;
    private ListView contactListView;
    private ContactAdapter contactAdapter;
    private ContactDao contactDao;
    private ArrayList<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.edt_search);
        contactListView = findViewById(R.id.lv_contacts);
        addButton = findViewById(R.id.btn_add);

        contactDao = new ContactDao(this);
        loadContacts();
        addButton.setOnClickListener(v -> showAddContactDialog());

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                searchContacts(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    public void loadContacts() {
        contactList = contactDao.getAllContacts();
        contactAdapter = new ContactAdapter(this, contactList);
        contactListView.setAdapter(contactAdapter);
    }

    public void searchContacts(String query) {
        ArrayList<Contact> fileteredList = contactDao.searchContacts(query);
        contactAdapter = new ContactAdapter(this, fileteredList);
        contactListView.setAdapter(contactAdapter);
    }

    public void showAddContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add_contact, null);

        nameEditText = view.findViewById(R.id.edt_add_name);
        phoneEditText = view.findViewById(R.id.edt_add_phone);
        Button saveButton = view.findViewById(R.id.btn_save_contact);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();

            if (name.isEmpty() || phone.length() != 10) {
                Toast.makeText(this, "Enter a valid name and 10-digit phone number", Toast.LENGTH_SHORT).show();
                return;
            }

            if(contactDao.addContact(new Contact(name, phone))) {
                Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loadContacts();
            } else {
                Toast.makeText(this, "Failed to Add", Toast.LENGTH_SHORT).show();
            }
        });
    }
}