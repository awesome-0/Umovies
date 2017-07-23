package com.example.samuel.umovies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samuel.umovies.Database.MovieContract;
import com.example.samuel.umovies.data.DetailsLoader;
import com.example.samuel.umovies.data.MoviesLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movies> {
    private TextView title_text;
    private TextView synopsis_text;
    private RatingBar ratingBar_details;
    private ImageView imageView_details;
    private TextView release_details;
    private TextView ratings_text;
    private ProgressBar progressBar;
    private TextView error_text;
    private static String movie_id = "";
    private static final int MOVIE_LOADER_ID =2;
    private TextView reviews;
    private LinearLayout details_layout;
    private LinearLayout review_layout;
    private static  int movieposition = 0;
    private ImageView favourite_star;
    private static Uri movieUri;
    private ArrayList<FavouritedMovies> favMovies = new ArrayList<>();
    private static  ArrayList<String> favMoviesId = new ArrayList<>();
   private static  ArrayList<String> favouritedMoviez  ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle info = getIntent().getExtras();
        progressBar = (ProgressBar) findViewById(R.id.details_progress_bar);
        error_text = (TextView) findViewById(R.id.details_error_text);
        Bundle bundle = getIntent().getExtras();
        Intent movieIntent = getIntent();
        movieUri = movieIntent.getData();
        movie_id = bundle.getString("id");
        details_layout = (LinearLayout) findViewById(R.id.details_layout);
        review_layout = (LinearLayout) findViewById(R.id.review_container);


        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID,null,this);


    }

    @Override
    public Loader<Movies> onCreateLoader(int id, Bundle args) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movie_id)
                //here you add your api key, had to remove my own
                .appendQueryParameter("api_key", "d48eb72bb65495581cfe2b9ab8ec9d88")
                .appendQueryParameter("language", "en-US")
                .appendQueryParameter("append_to_response","videos,reviews")
                .appendQueryParameter("page", "1").build();
        Log.d("movie Url",builder.toString());

        if (isConnected()) {
            error_text.setText("");

            return new DetailsLoader(this, builder.toString());
        } else {

            noInternetConnection();
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Movies> loader, Movies data) {
        if (data != null) {
            progressBar.setVisibility(View.GONE);
            details_layout.setVisibility(View.VISIBLE);
            extractMovieDetails(data);

        }
        if(data == null){
            showError();
        }

    }

    @Override
    public void onLoaderReset(Loader<Movies> loader) {

    }

    public void extractMovieDetails(final Movies movie) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this);
        final SharedPreferences.Editor edit = preferences.edit();
        final Gson gson = new Gson();


        String title = movie.getTitle();
        String synopsis = movie.getSynopsis();
        String rating = movie.getUser_rating();
        String release = movie.getRelease_date();
        String image = movie.getImage();
        String review = movie.getReview();
        String reviewer = movie.getReviewer();
        final String video_link = movie.getVideo();
        title_text = (TextView) findViewById(R.id.details_title);
        synopsis_text = (TextView) findViewById(R.id.details_synopsis);
        imageView_details = (ImageView) findViewById(R.id.details_image);
        ratingBar_details = (RatingBar) findViewById(R.id.details_rating_bar);
        release_details = (TextView) findViewById(R.id.details_year);
        ratings_text = (TextView) findViewById(R.id.rating_text);
        favourite_star = (ImageView) findViewById(R.id.favourite);
        if(favouritedMoviez == null){
            favourite_star.setColorFilter(ContextCompat.getColor(DetailsActivity.this,R.color.star_unchecked));
        }
        else {
            String json = preferences.getString("favourited", "");
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            favouritedMoviez  = gson.fromJson(json, type);
            for (String x : favouritedMoviez) {
                if (x.equals(movie.getId())) {
                    favourite_star.setColorFilter(ContextCompat.getColor(DetailsActivity.this, R.color.samuel));
                }

            }
        }

        reviews = (TextView) findViewById(R.id.reviews);
        if(!review.isEmpty() && review != null){
            String concatReview = review + "  - " + reviewer;
            reviews.setText(concatReview);

        }
        else{
            reviews.setText("No Reviews yet");
        }
        title_text.setText(title);
        String concatRating = rating + " / " + 10;
        ratings_text.setText(concatRating);

        String format = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
        try {
            format = sdf.format(new SimpleDateFormat("yyyy-MM-dd").parse(release));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        release_details.setText(format);
        synopsis_text.setText(synopsis);
        ratingBar_details.setRating(Float.parseFloat(rating) / 2);
        String imageUrl = ("https://image.tmdb.org/t/p/w185" + image);
        Picasso.with(this).load(imageUrl).into(imageView_details);
        imageView_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(Intent.ACTION_VIEW);
                String youtube = "https://www.youtube.com/watch?v=" + video_link;
                videoIntent.setData(Uri.parse(youtube));
                startActivity(videoIntent);
            }
        });
        favourite_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Id = movie.getId();
                if(favouritedMoviez == null || favouritedMoviez.size() == 0){
                    favouritedMoviez = new ArrayList<String>();
                    favourite_star.setColorFilter(ContextCompat.getColor(DetailsActivity.this,R.color.samuel));
                    Toast.makeText(DetailsActivity.this,"Added to Favourites",Toast.LENGTH_SHORT).show();
                    favouritedMoviez.add(Id);
                    String json = gson.toJson(favouritedMoviez);
                    edit.putString("favourited",json);
                    edit.commit();
                    ContentValues values = new ContentValues();
                    values.put(MovieContract.MovieEntry.COLUMN_TITLE,movie.getTitle());
                    values.put(MovieContract.MovieEntry.COLUMN_IMAGE,movie.getImage());
                    values.put(MovieContract.MovieEntry.COLUMN_VIDEO,movie.getVideo());
                    values.put(MovieContract.MovieEntry.COLUMN_REVIEW,movie.getReview());
                    values.put(MovieContract.MovieEntry.COLUMN_REVIEWER,movie.getReview());
                    values.put(MovieContract.MovieEntry.COLUMN_RATING,movie.getUser_rating());
                    values.put(MovieContract.MovieEntry.COLUMN_RELEASEDATE,movie.getRelease_date());
                    Uri insert = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
                    Log.e("Uri",insert.toString());


                    return;

                }
                else{
              //  for(String x: favouritedMoviez) {
                    int indexOfMOvie = favouritedMoviez.indexOf(Id);
                    if(indexOfMOvie != -1){

                        String json = preferences.getString("favourited", "");
                        Type type = new TypeToken<ArrayList<String>>() {
                        }.getType();
                        favouritedMoviez = gson.fromJson(json, type);


                        favouritedMoviez.remove(favouritedMoviez.get(indexOfMOvie));
                        String jsOn = gson.toJson(favouritedMoviez);
                        edit.putString("favourited", jsOn);
                        edit.commit();
                        int delete = getContentResolver().delete(movieUri, null, null);

                        favourite_star.setColorFilter(ContextCompat.getColor(DetailsActivity.this, R.color.star_unchecked));
                        Toast.makeText(DetailsActivity.this, "Removed from Favourites", Toast.LENGTH_SHORT).show();
                    } else {
                        favourite_star.setColorFilter(ContextCompat.getColor(DetailsActivity.this, R.color.samuel));
                        Toast.makeText(DetailsActivity.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
                        String json = preferences.getString("favourited", "");
                        Type type = new TypeToken<ArrayList<String>>() {
                        }.getType();
                        favouritedMoviez = gson.fromJson(json, type);
                        favouritedMoviez.add(Id);
                        String jSon = gson.toJson(favouritedMoviez);
                        edit.putString("favourited", jSon);
                        edit.commit();
                        ContentValues values = new ContentValues();
                        values.put(MovieContract.MovieEntry.COLUMN_TITLE,movie.getTitle());
                        values.put(MovieContract.MovieEntry.COLUMN_IMAGE,movie.getImage());
                        values.put(MovieContract.MovieEntry.COLUMN_VIDEO,movie.getVideo());
                        values.put(MovieContract.MovieEntry.COLUMN_REVIEW,movie.getReview());
                        values.put(MovieContract.MovieEntry.COLUMN_REVIEWER,movie.getReview());
                        values.put(MovieContract.MovieEntry.COLUMN_RATING,movie.getUser_rating());
                        values.put(MovieContract.MovieEntry.COLUMN_RELEASEDATE,movie.getRelease_date());
                        Uri insert = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
                        Log.e("Uri",insert.toString());

                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
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

    public void Favourited(View view) {

    }
    public class FavouritedMovies{
        Movies favouritedmovie;
        String ID = "";

        public Movies getFavouritedmovie() {
            return favouritedmovie;
        }

        public String getID() {
            return ID;
        }

        public FavouritedMovies(Movies favouritedmovie, String ID) {
            this.favouritedmovie = favouritedmovie;
            this.ID = ID;


        }
    }
}

