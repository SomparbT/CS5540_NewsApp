package com.example.android.newsapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Siriporn on 7/23/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "news.db";
    private static final String TAG = "dbhelper";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create new database
        String queryString = "CREATE TABLE " + Contract.TABLE_NEWS.TABLE_NAME + " ("+
                Contract.TABLE_NEWS._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.TABLE_NEWS.COLUMN_NAME_AUTHOR + " TEXT, " +
                Contract.TABLE_NEWS.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                Contract.TABLE_NEWS.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                Contract.TABLE_NEWS.COLUMN_NAME_URL + " TEXT, " +
                Contract.TABLE_NEWS.COLUMN_NAME_URL_TO_IMAGE + " TEXT, " +
                Contract.TABLE_NEWS.COLUMN_NAME_DATE + " DATE" +
                "); ";

        Log.d(TAG, "Create table SQL: " + queryString);
        db.execSQL(queryString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //create new database when upgrade
        db.execSQL("DROP TABLE IF EXISTS " + Contract.TABLE_NEWS.TABLE_NAME);
        onCreate(db);
    }
}

