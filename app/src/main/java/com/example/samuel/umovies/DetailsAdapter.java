package com.example.samuel.umovies;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Samuel on 21/07/2017.
 */

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.detailsHolder> {
    private Movies movie;
    private Context context;

    public DetailsAdapter( Context context,Movies movie) {
        this.movie = movie;
        this.context = context;
    }

    @Override
    public detailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View DetailsView = LayoutInflater.from(context).inflate(R.layout.activity_details,parent,false);

        detailsHolder holder = new detailsHolder(DetailsView);
        return holder;
    }

    @Override
    public void onBindViewHolder(detailsHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class detailsHolder extends RecyclerView.ViewHolder{
        private TextView title_text;
        private TextView synopsis_text;
        private RatingBar ratingBar_details;
        private ImageView imageView_details;
        private TextView release_details;
        private TextView ratings_text;


        public detailsHolder(View itemView) {
            super(itemView);
            title_text = (TextView) itemView.findViewById(R.id.details_title);
        synopsis_text = (TextView) itemView.findViewById(R.id.details_synopsis);
        imageView_details = (ImageView) itemView.findViewById(R.id.details_image);
        ratingBar_details = (RatingBar) itemView.findViewById(R.id.details_rating_bar);
        release_details = (TextView) itemView.findViewById(R.id.details_year);
        ratings_text = (TextView) itemView.findViewById(R.id.rating_text);
        }
    }
}
