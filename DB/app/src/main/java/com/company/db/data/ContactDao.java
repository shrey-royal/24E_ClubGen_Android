package com.company.db.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.company.db.model.Contact;

import java.util.ArrayList;

public class ContactDao {
    private SQLiteDatabase db;
    private DatabaseConnection dbHelper;

    public ContactDao(Context context) {
        dbHelper = new DatabaseConnection(context);
    }

    //Open Database
    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean addContact(Contact contact) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_NAME, contact.getName());
        values.put(DatabaseConnection.COLUMN_PHONE, contact.getPhone());

        long result = db.insert(DatabaseConnection.TABLE_NAME, null, values);
        close();

        return (result != -1);
    }

    public ArrayList<Contact> getAllContacts() {
        return null;
    }

    public ArrayList<Contact> searchContacts(String query) {
        return null;
    }

    public boolean updateContact(Contact contact) {
        return false;
    }

    public boolean deleteContact(int id) {
        open();
        int rowsDeleted = db.delete(DatabaseConnection.TABLE_NAME, DatabaseConnection.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        close();
        return rowsDeleted > 0;
    }
}
