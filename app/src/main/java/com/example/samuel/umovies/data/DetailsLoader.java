package com.example.samuel.umovies.data;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.samuel.umovies.Movies;

/**
 * Created by Samuel on 21/07/2017.
 */

public class DetailsLoader extends AsyncTaskLoader<Movies> {
    String url;
    public DetailsLoader(Context context,String url) {
        super(context);
        this.url = url;
    }

    @Override
    public Movies loadInBackground() {
        Movies movie =  FetchDetailsDataFromInternet.extractMovieFromJsonResponse(url);
        return movie;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
