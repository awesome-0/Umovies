package com.example.samuel.umovies;


import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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

import com.example.samuel.umovies.data.MoviesLoader;

import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movies>>,SharedPreferences.OnSharedPreferenceChangeListener {
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private static final int MOVIE_LOADER_ID =1;
    private TextView error_text;
    private ArrayList<Movies> movies = null;

    GridLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) findViewById(R.id.Recycler_view);
        error_text = (TextView) findViewById(R.id.error_text);
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID,null,this);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
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
                .appendQueryParameter("api_key",getString(R.string.api_key))
                .appendQueryParameter("language","en-US")
                .appendQueryParameter("page","1").build();
        if(isConnected()){

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
    public void onLoaderReset(Loader<ArrayList<Movies>> loader) {loader.reset();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.list_preference_key))){
            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID,null,this);
        }
    }
}
