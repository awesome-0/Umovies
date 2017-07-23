package com.example.samuel.umovies;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.umovies.Database.MovieContract;
import com.example.samuel.umovies.Movies;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Samuel on 19/07/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.movieHolder> {
    ArrayList<Movies>movies = new ArrayList<>();
    final Context context;



    public MovieAdapter(Context context,ArrayList<Movies>movies) {
        this.movies = movies;
        this.context = context;

    }

    @Override
    public movieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View movieView = LayoutInflater.from(context).inflate(R.layout.grid_layout,parent,false);
        movieHolder holder = new movieHolder(movieView);
        return holder;
    }

    @Override
    public void onBindViewHolder(movieHolder holder, int position) {
        String title = movies.get(position).getTitle();
        String release = movies.get(position).getRelease_date();
        String substring = release.substring(0,4);
        String image = movies.get(position).getImage();
       holder.display_title.setText(title);
        holder.display_year.setText(substring);
        String imageUrl = ("https://image.tmdb.org/t/p/w154" + image);
        Picasso.with(context).load(imageUrl).into(holder.display_image);
        holder.itemView.setTag(movies.get(position));



    }

    @Override
    public int getItemCount() {
        return movies.size();
    }



    public static class movieHolder extends RecyclerView.ViewHolder{

      ImageView display_image;
      TextView display_title;
      TextView display_year;

      public movieHolder(final View itemView) {
          super(itemView);
          display_image = (ImageView) itemView.findViewById(R.id.display_image);
          display_title = (TextView) itemView.findViewById(R.id.display_title);
          display_year = (TextView) itemView.findViewById(R.id.display_year);
          display_image.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent detailsIntent = new Intent(itemView.getContext(),DetailsActivity.class);
                  Movies movie = (Movies) itemView.getTag();
                  detailsIntent.putExtra("id",movie.getId());
                  int position = getAdapterPosition();
                 detailsIntent.setData(ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI,position));
                  itemView.getContext().startActivity(detailsIntent);


              }
          });
      }
  }
}
