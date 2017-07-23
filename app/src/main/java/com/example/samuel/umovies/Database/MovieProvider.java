package com.example.samuel.umovies.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import com.example.samuel.umovies.Database.MovieContract.MovieEntry;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Samuel on 22/07/2017.
 */

public class MovieProvider extends ContentProvider {
    private MovieDb mDbHelper;
    SQLiteDatabase db ;
    private static final int MOVIES = 100;
    private static final int MOVIE = 101;
    private static UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH,MOVIES);
        sUriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH + "/#",MOVIE);


    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDb(getContext());
        return true;


    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
       db = mDbHelper.getReadableDatabase();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                cursor = db.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case MOVIE:
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri.toString());
        }

                cursor.setNotificationUri(getContext().getContentResolver(),uri);
                return cursor;




        }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        db = mDbHelper.getWritableDatabase();
        long insert;
        switch (sUriMatcher.match(uri)){
            case MOVIES:
                 insert = db.insert(MovieEntry.TABLE_NAME, null, values);

                break;

            default:
                throw new IllegalArgumentException("Unknown Uri" + uri.toString());
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,insert);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        db = mDbHelper.getWritableDatabase();
        int delete;
        switch (sUriMatcher.match(uri)){
            case MOVIES:
                 delete = db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE:
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String []{String.valueOf(ContentUris.parseId(uri))};
                delete  = db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri" + uri.toString());
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return delete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        db = mDbHelper.getWritableDatabase();
        int update;
        switch (sUriMatcher.match(uri)){
            case MOVIES:
                update = db.update(MovieEntry.TABLE_NAME,values, selection, selectionArgs);
                break;
            case MOVIE:
                selection = MovieEntry._ID + "=?";
                selectionArgs = new String []{String.valueOf(ContentUris.parseId(uri))};
                update  = db.update(MovieEntry.TABLE_NAME,values, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown Uri" + uri.toString());
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return update;
    }
}
