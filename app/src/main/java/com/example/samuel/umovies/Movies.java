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
    private String id;
    private String video;
    private String reviewer;
    private String review;

    public Movies() {
    }



    public Movies(String title, String image, String user_rating, String synopsis, String release_date, String id,String video,String reviewer,String review) {
        this.title = title;
        this.image = image;
        this.user_rating = user_rating;
        this.synopsis = synopsis;
        this.release_date = release_date;
        this.video = video;
        this.id = id;
        this.review = review;
        this.reviewer = reviewer;
    }
    public Movies(String title, String image, String user_rating, String synopsis, String release_date, String id) {
        this.title = title;
        this.image = image;
        this.user_rating = user_rating;
        this.synopsis = synopsis;
        this.release_date = release_date;
        this.id = id;
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
    public String getId() {
        return id;
    }

    public String getVideo() {
        return video;
    }

    public String getReviewer() {
        return reviewer;
    }

    public String getReview() {
        return review;
    }
}
