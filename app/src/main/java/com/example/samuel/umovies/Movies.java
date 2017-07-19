package com.example.samuel.umovies;

/**
 * Created by Samuel on 19/07/2017.
 */

public class Movies {
    private String title;
    private String image;
    private String user_rating;
    private String synopsis;
    private String release_date;

    public Movies(String title, String image, String user_rating, String synopsis, String release_date) {
        this.title = title;
        this.image = image;
        this.user_rating = user_rating;
        this.synopsis = synopsis;
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getRelease_date() {
        return release_date;
    }
}
