package com.example.samuel.umovies.data;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.samuel.umovies.Movies;

import java.util.ArrayList;

/**
 * Created by Samuel on 19/07/2017.
 */

public class MoviesLoader extends AsyncTaskLoader<ArrayList<Movies>> {
    private String mUrl;
    public MoviesLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public ArrayList<Movies> loadInBackground() {
        return FetchDataFromInternet.extractMovieFromJsonResponse(mUrl);

    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
