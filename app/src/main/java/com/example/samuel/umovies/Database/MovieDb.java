package com.example.samuel.umovies.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.samuel.umovies.Database.MovieContract.MovieEntry;

/**
 * Created by Samuel on 22/07/2017.
 */

public class MovieDb extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "movies.db";
    public static final int DATABASE_VERSION = 1;
    public MovieDb(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String command = "CREATE TABLE "+ MovieEntry.TABLE_NAME + "("+
                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                MovieEntry.COLUMN_IMAGE + " TEXT NOT NULL," +
                MovieEntry.COLUMN_VIDEO + " TEXT NOT NULL," +
                MovieEntry.COLUMN_RELEASEDATE + " TEXT NOT NULL," +
                MovieEntry.COLUMN_RATING + " TEXT NOT NULL," +
                MovieEntry.COLUMN_REVIEW + " TEXT NOT NULL," +
                MovieEntry.COLUMN_REVIEWER + " TEXT NOT NULL" + ");";
        db.execSQL(command);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
