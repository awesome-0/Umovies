package com.example.samuel.umovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.samuel.umovies.Movies;

import java.util.ArrayList;

/**
 * Created by Samuel on 19/07/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.movieHolder> {
    ArrayList<Movies>movies = new ArrayList<>();
    Context context;

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

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class movieHolder extends RecyclerView.ViewHolder{

      ImageView display_image;
      TextView display_title;
      TextView display_year;

      public movieHolder(View itemView) {
          super(itemView);
          display_image = (ImageView) itemView.findViewById(R.id.display_image);
          display_title = (TextView) itemView.findViewById(R.id.display_title);
          display_year = (TextView) itemView.findViewById(R.id.display_year);
      }
  }
}
