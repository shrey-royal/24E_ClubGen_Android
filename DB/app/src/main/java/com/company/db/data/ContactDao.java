package com.company.db.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
        open();
        ArrayList<Contact> contactsList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseConnection.TABLE_NAME, null, null, null, null, null, DatabaseConnection.COLUMN_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do{
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DatabaseConnection.COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_NAME));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_PHONE));
                contactsList.add(new Contact(id, name, phone));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return contactsList;
    }

    public ArrayList<Contact> searchContacts(String query) {
        open();
        ArrayList<Contact> contactsList = new ArrayList<>();
        Cursor cursor = db.query(DatabaseConnection.TABLE_NAME, null, DatabaseConnection.COLUMN_NAME + " LIKE ?", new String[]{"%" + query + "%"}, null, null, DatabaseConnection.COLUMN_NAME + " ASC");

        if (cursor.moveToFirst()) {
            do{
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DatabaseConnection.COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_NAME));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_PHONE));
                contactsList.add(new Contact(id, name, phone));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close();
        return contactsList;
    }

    public boolean updateContact(Contact contact) {
        open();
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_NAME, contact.getName());
        values.put(DatabaseConnection.COLUMN_PHONE, contact.getPhone());

        int rowsAffected = db.update(DatabaseConnection.TABLE_NAME, values, DatabaseConnection.COLUMN_ID + " = ?", new String[]{String.valueOf(contact.getId())});
        close();
        return rowsAffected > 0;
    }

    public boolean deleteContact(int id) {
        open();
        int rowsDeleted = db.delete(DatabaseConnection.TABLE_NAME, DatabaseConnection.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        close();
        return rowsDeleted > 0;
    }
}
