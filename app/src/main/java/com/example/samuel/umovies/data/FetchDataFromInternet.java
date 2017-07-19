package com.example.samuel.umovies.data;

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
 * Created by Samuel on 19/07/2017.
 */

public class FetchDataFromInternet {

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
            if(connection.getResponseCode() == 200){

                stream = connection.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(stream);
                reader = new BufferedReader(streamReader);
                StringBuffer buffer = new StringBuffer();

                String line = "";
                while(( line = reader.readLine())!= null){
                    buffer.append(line);

                }
                return buffer.toString();
            }
            return  null;
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
    public static ArrayList<Movies> extractMovieFromJsonResponse(String urL){
        String jsonResponse = FetchJsonResponse(urL);
       ArrayList<Movies> movie = new ArrayList<Movies>();

        if(jsonResponse == null)return null;
        try {

            JSONObject rootJsonObject = new JSONObject(jsonResponse);
            JSONArray rootArray = rootJsonObject.getJSONArray("results");
            for(int i = 0;i<rootArray.length();i++){
             JSONObject actual_object = rootArray.getJSONObject(i);
                String title = actual_object.getString("original_title");
                String release_date = actual_object.getString("release_date");
                String image = actual_object.getString("poster_path");
                String synopsis = actual_object.getString("overview");
                String rating = actual_object.getString("vote_average");
                movie.add(new Movies(title,image,rating,synopsis,release_date));

            }
            return movie;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }
}
