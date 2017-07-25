package com.example.samuel.umovies.data;

import android.util.Log;

import com.example.samuel.umovies.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Samuel on 21/07/2017.
 */

public class FetchDetailsDataFromInternet {
    public static URL CreateUrl(String url){
        URL api_url = null;
        try {
            api_url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }

        return api_url;

    }

    public static String FetchJsonResponse(String urL){
        URL url = CreateUrl(urL);
        HttpURLConnection connection = null;
        InputStream stream = null;

        BufferedReader reader = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            stream = connection.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(stream);
            reader = new BufferedReader(streamReader);
            StringBuffer buffer = new StringBuffer();

            String line = "";
            while(( line = reader.readLine())!= null){
                buffer.append(line);
            }
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();

        }finally {
            if(connection != null){
                connection.disconnect();
            }
            if(stream !=null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public static Movies extractMovieFromJsonResponse(String urL){
        String jsonResponse = FetchJsonResponse(urL);
       Movies movie;

        if(jsonResponse == null)return null;
        try {

            JSONObject rootJsonObject = new JSONObject(jsonResponse);

                String title = rootJsonObject.getString("original_title");
                String release_date = rootJsonObject.getString("release_date");
                String image = rootJsonObject.getString("poster_path");
                String synopsis = rootJsonObject.getString("overview");
                String rating = rootJsonObject.getString("vote_average");
                String id = rootJsonObject.getString("id");
            JSONObject resultsObject = rootJsonObject.getJSONObject("videos");
            JSONArray resultsArray = resultsObject.getJSONArray("results");
            JSONObject videos = resultsArray.getJSONObject(0);
            String video_key = videos.getString("key");
            JSONObject reviewObject = rootJsonObject.getJSONObject("reviews");
            String num_of_reviews = reviewObject.getString("total_results");
            String reviewer = "";
            String review = "";
            if(num_of_reviews.equals("0")){
                return  new Movies(title,image,rating,synopsis,release_date,id,video_key,reviewer,review);

            }
            else {
                JSONArray reviewsArray = reviewObject.getJSONArray("results");

                JSONObject ReviewObject = reviewsArray.getJSONObject(0);
                if (ReviewObject.has("author")) {
                    reviewer = ReviewObject.getString("author");
                }
                if (ReviewObject.has("content")) {
                    review = ReviewObject.getString("content");
                }

                return new Movies(title, image, rating, synopsis, release_date, id, video_key, reviewer, review);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
}
