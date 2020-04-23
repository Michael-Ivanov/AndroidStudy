package com.android.tvchannelsadapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbOpenHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "channels.db";
    public static final String DB_TABLE = "channels";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_RATING = "rating";

    public static final String DB_CREATE = " CREATE TABLE " + DB_TABLE +
            " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "  +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_URL + " TEXT NOT NULL,  " +
            COLUMN_RATING + " FLOAT NOT NULL);";



    public DbOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
