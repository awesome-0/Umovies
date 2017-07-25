package com.example.samuel.umovies;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samuel.umovies.Database.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by Samuel on 21/07/2017.
 */

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.detailsHolder> {
    private Cursor cursor;
    private Context context;

    public FavouritesAdapter(Context context, Cursor cursor) {
        this.cursor = cursor;
        this.context = context;
    }

    @Override
    public detailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View DetailsView = LayoutInflater.from(context).inflate(R.layout.grid_layout,parent,false);
       return new detailsHolder(DetailsView);

    }

    @Override
    public void onBindViewHolder(detailsHolder holder, int position) {
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            cursor.moveToPosition(position);
            Log.v("cursor number", cursor.getCount() + "");

            String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
            String year = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASEDATE));
           byte[] image = cursor.getBlob(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE));
            holder.display_title_f.setText(title);
            String substring = year.substring(0, 4);
            holder.display_year_f.setText(substring);
            holder.display_image_f.setImageBitmap(BitmapUtil.getBitmap(image));
            int id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry._ID));
            holder.itemView.setTag(id);

        }
    }
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class detailsHolder extends RecyclerView.ViewHolder{
        ImageView display_image_f;
        TextView display_title_f;
        TextView display_year_f;


        public detailsHolder(final View itemView) {
            super(itemView);
            display_image_f = (ImageView) itemView.findViewById(R.id.display_image);
            display_title_f = (TextView) itemView.findViewById(R.id.display_title);
            display_year_f = (TextView) itemView.findViewById(R.id.display_year);
            display_image_f.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailsIntent = new Intent(itemView.getContext(), DetailsActivity.class);
                    int id = (int) itemView.getTag();
                    Uri uri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI, id);
                    String movieUri = uri.toString();
                    detailsIntent.putExtra("fav_movie",movieUri);
                    itemView.getContext().startActivity(detailsIntent);
                }
                });
    }

}
}
