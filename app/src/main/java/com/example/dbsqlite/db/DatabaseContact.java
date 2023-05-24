package com.example.dbsqlite.db;

import static com.example.dbsqlite.util.Constants.DB_NAME;
import static com.example.dbsqlite.util.Constants.DB_VERSION;
import static com.example.dbsqlite.util.Constants.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.dbsqlite.model.Contact;
import com.example.dbsqlite.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DatabaseContact extends SQLiteOpenHelper implements DBService {


    public DatabaseContact(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ( " +
                Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.NAME + " TEXT, " +
                Constants.PHONE_NUMBER + " TEXT UNIQUE"
                + " )";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;

        db.execSQL(query);

        onCreate(db);

    }

    @Override
    public boolean insertContact(Contact contact) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.NAME, contact.getName());
        values.put(Constants.PHONE_NUMBER, contact.getPhoneNumber());

        long result = db.insert(TABLE_NAME, null, values);

        db.close();

        return result != -1;
    }

    @Override
    public boolean updateContact(Contact contact) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.NAME, contact.getName());
        values.put(Constants.PHONE_NUMBER, contact.getPhoneNumber());

        db.update(TABLE_NAME, values, Constants.ID + "=?", new String[]{String.valueOf(contact.getId())});
        db.close();

        return true;
    }

    @Override
    public boolean deleteContact(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, Constants.ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return true;
    }

    @Override
    public List<Contact> getAllContacts() {

        List<Contact> contacts = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            do {
                Contact contact = new Contact();

                contact.setId(cursor.getInt(0));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));

                contacts.add(contact);

            } while (cursor.moveToNext());

        }

        return contacts;
    }

    @Override
    public Contact getContactById() {
        return new Contact();
    }
}
