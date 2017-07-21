package com.example.samuel.umovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DetailsActivity extends AppCompatActivity {
    private TextView title_text;
    private TextView synopsis_text;
    private RatingBar ratingBar_details;
    private ImageView imageView_details;
    private TextView release_details;
    private TextView ratings_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle info = getIntent().getExtras();
        title_text = (TextView) findViewById(R.id.details_title);
        synopsis_text = (TextView) findViewById(R.id.details_synopsis);
        imageView_details = (ImageView) findViewById(R.id.details_image);
        ratingBar_details = (RatingBar) findViewById(R.id.details_rating_bar);
        release_details = (TextView) findViewById(R.id.details_year);
        ratings_text = (TextView) findViewById(R.id.rating_text);
        String title = info.getString("title", "");
        String synopsis = info.getString("synopsis", "");
        String rating = info.getString("rating", "");
        String release = info.getString("release", "");
        String image = info.getString("image", "");
        title_text.setText(title);
        String concatRating = rating + "/ " +10;
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
        ratingBar_details.setRating(Float.parseFloat(rating)/2);
        String imageUrl = ("https://image.tmdb.org/t/p/w185" + image);
        Picasso.with(this).load(imageUrl).into(imageView_details);


    }
}
