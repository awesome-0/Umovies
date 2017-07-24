package com.example.samuel.umovies;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.samuel.umovies.Database.MovieContract;
import com.example.samuel.umovies.data.MoviesLoader;
import java.util.ArrayList;

/*
*   Designed by Samuel Orji
*   awesomeorji@gmail.com
*
*
 */

public class MovieActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movies>>,SharedPreferences.OnSharedPreferenceChangeListener, SwipeRefreshLayout.OnRefreshListener {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private static final int MOVIE_LOADER_ID =1;
    private TextView error_text;
    private ArrayList<Movies> movies = null;
    private SwipeRefreshLayout refreshLayout;
    private AsyncTask dbAsyncTask;

    GridLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) findViewById(R.id.Recycler_view);
        error_text = (TextView) findViewById(R.id.error_text);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_container);
        refreshLayout.setOnRefreshListener(this);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value = defaultSharedPreferences.getString(getString(R.string.list_preference_key),"");
        if(value.equals(getString(R.string.favourited))){
            new FavouriteAsyncTask().execute();
                  }
        else {

            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Intent settingsIntent = new Intent(this,Settings.class);
                startActivity(settingsIntent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(CONNECTIVITY_SERVICE);

       NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
       SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value = defaultSharedPreferences.getString(getString(R.string.list_preference_key),"");
        if(value.equals(getString(R.string.favourited))){
            getSupportLoaderManager().destroyLoader(MOVIE_LOADER_ID);
            new FavouriteAsyncTask().execute();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID,null,this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value = defaultSharedPreferences.getString(getString(R.string.list_preference_key),"");
        if(value.equals(getString(R.string.favourited))){
            getSupportLoaderManager().destroyLoader(MOVIE_LOADER_ID);
            new FavouriteAsyncTask().execute();
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID,null,this);
        }

    }

    private void noInternetConnection() {
        progressBar.setVisibility(View.GONE);
        error_text.setText(R.string.no_internet);
        error_text.setVisibility(View.VISIBLE);

    }

    private void showError() {
        progressBar.setVisibility(View.GONE);
        error_text.setText(R.string.broken_connection);
        error_text.setVisibility(View.VISIBLE);


    }


    @Override
    public Loader<ArrayList<Movies>> onCreateLoader(int id, Bundle args) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String criteria = preferences.getString(getString(R.string.list_preference_key),"");
        Log.e("criteria",criteria);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(criteria)
                //here you add your api key, had to remove my own
                .appendQueryParameter("api_key","d48eb72bb65495581cfe2b9ab8ec9d88")
                .appendQueryParameter("language","en-US")
                .appendQueryParameter("page","1").build();
        if(isConnected()){
            error_text.setText("");

            return new MoviesLoader(this,builder.toString());
        }
        else{

            noInternetConnection();
            return null;
        }





    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movies>> loader, ArrayList<Movies> data) {
        if (data != null) {
            if(!data.isEmpty()) {
                progressBar.setVisibility(View.GONE);
                adapter = new MovieAdapter(this, data);
                if(movies != null){
                movies.clear();
                movies.addAll(data);}
                else{
                    movies = data;
                }
                adapter =new MovieAdapter(this,movies);
                manager = new GridLayoutManager(this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
            }
        }
        if(data == null){
            showError();
        }


    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movies>> loader) {
        loader.reset();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            String Tag = "SHared Pref changed";

            String value = sharedPreferences.getString(getString(R.string.list_preference_key),"");
            Log.d(Tag,value);
            if(value.equals(getString(R.string.favourited))){
                getSupportLoaderManager().destroyLoader(MOVIE_LOADER_ID);
                new FavouriteAsyncTask().execute();
            }
            else{
                progressBar.setVisibility(View.VISIBLE);
                getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID,null,this);
            }

    }

    @Override
    public void onRefresh() {
        getSupportLoaderManager().destroyLoader(MOVIE_LOADER_ID);
        progressBar.setVisibility(View.VISIBLE);
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID,null,this);
        refreshLayout.setRefreshing(false);
    }

    public Cursor getFavourites(){
        String[] projection = {MovieContract.MovieEntry._ID,MovieContract.MovieEntry.COLUMN_TITLE, MovieContract.MovieEntry.COLUMN_IMAGE,
                MovieContract.MovieEntry.COLUMN_VIDEO,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                MovieContract.MovieEntry.COLUMN_REVIEW,
                MovieContract.MovieEntry.COLUMN_REVIEWER,
                MovieContract.MovieEntry.COLUMN_RATING,
                MovieContract.MovieEntry.COLUMN_SYNOPSIS,
                MovieContract.MovieEntry.COLUMN_RELEASEDATE};
        return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, projection, null, null, MovieContract.MovieEntry.ID);

    }

    public class FavouriteAsyncTask extends AsyncTask<Cursor,Void,Cursor>{


        @Override
        protected Cursor doInBackground(Cursor... params) {
            return getFavourites();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            if(cursor.getCount() == 0 ){
                progressBar.setVisibility(View.GONE);

                error_text.setText("No movies Favourited");
                error_text.setVisibility(View.VISIBLE);
                MovieAdapter ad = new MovieAdapter(MovieActivity.this,new ArrayList<Movies>());
                manager = new GridLayoutManager(MovieActivity.this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(ad);


            }
            else{

                Log.d("cursor",""+cursor.getCount());
                progressBar.setVisibility(View.GONE);

                FavouritesAdapter dAdapter = new FavouritesAdapter(MovieActivity.this,cursor);
                manager = new GridLayoutManager(MovieActivity.this,2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(dAdapter);
            }

        }

        }
    }



