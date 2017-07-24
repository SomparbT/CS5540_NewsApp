package com.example.android.newsapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static com.example.android.newsapp.data.Contract.TABLE_NEWS.COLUMN_NAME_AUTHOR;
import static com.example.android.newsapp.data.Contract.TABLE_NEWS.COLUMN_NAME_DATE;
import static com.example.android.newsapp.data.Contract.TABLE_NEWS.COLUMN_NAME_DESCRIPTION;
import static com.example.android.newsapp.data.Contract.TABLE_NEWS.COLUMN_NAME_TITLE;
import static com.example.android.newsapp.data.Contract.TABLE_NEWS.COLUMN_NAME_URL;
import static com.example.android.newsapp.data.Contract.TABLE_NEWS.COLUMN_NAME_URL_TO_IMAGE;
import static com.example.android.newsapp.data.Contract.TABLE_NEWS.TABLE_NAME;

/**
 * Created by Siriporn on 7/23/2017.
 */

public class DatabaseUtils {

    //method for retrieve all news from database sort by date
    public static Cursor getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_NAME_DATE + " DESC"
        );
        return cursor;
    }

    public static void bulkInsert(SQLiteDatabase db, ArrayList<NewsItem> newsItems) {
        // method for insert all news from network to database
        db.beginTransaction();
        try {
            for (NewsItem newsItem : newsItems) {
                ContentValues cv = new ContentValues();
                cv.put(COLUMN_NAME_AUTHOR, newsItem.getAuthor());
                cv.put(COLUMN_NAME_TITLE, newsItem.getTitle());
                cv.put(COLUMN_NAME_DESCRIPTION, newsItem.getDescription());
                cv.put(COLUMN_NAME_URL, newsItem.getUrl());
                cv.put(COLUMN_NAME_URL_TO_IMAGE, newsItem.getUrlToImage());
                cv.put(COLUMN_NAME_DATE, newsItem.getDate());
                db.insert(TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public static void deleteAll(SQLiteDatabase db) {
        //method for delete database
        db.delete(TABLE_NAME, null, null);
    }

}

