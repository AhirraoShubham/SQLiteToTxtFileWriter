package com.example.acer.sqlitetotxtfilewriter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Your database name
    public static final String DATABASE_NAME = "employee.db";
    //Your table name
    public static final String TABLE_NAME = "tbl_emp";

    //Column Names of your table
    public static final String COL_ID = "ID";
    public static final String COL_FNAME = "FNAME";
    public static final String COL_LNAME = "LNAME";
    public static final String COL_CITY = "CITY";
    public static final String COL_SALARY = "SALARY";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,FNAME TEXT,LNAME TEXT,CITY TEXT,SALARY NUMBER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Inserting record in SQLite database
    public boolean insertData(String fname, String lname, String city, String salary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FNAME, fname);
        contentValues.put(COL_LNAME, lname);
        contentValues.put(COL_CITY, city);
        contentValues.put(COL_SALARY, salary);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Get all record from database
    public Cursor getAllRecord() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }
}
