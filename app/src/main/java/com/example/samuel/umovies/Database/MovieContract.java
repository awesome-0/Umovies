package com.example.samuel.umovies.Database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Samuel on 22/07/2017.
 */

public class MovieContract {


    private MovieContract(){};
    public static final String AUTHORITY = "com.example.samuel.umovies";
    public  static final String PATH = "movies";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);




    public static class MovieEntry implements BaseColumns{


        public  static final Uri CONTENT_URI  = Uri.withAppendedPath(BASE_URI,PATH);
        public static final String TABLE_NAME = "movies";
        public static final String ID = BaseColumns._ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_REVIEW = "review";
        public static final String COLUMN_REVIEWER = "reviewer";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASEDATE = "release_Date";


    }
}
