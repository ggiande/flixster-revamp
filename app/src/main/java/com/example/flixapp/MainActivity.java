package com.example.flixapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.AbsListView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixapp.adapters.MovieItemAdapter;
import com.example.flixapp.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {
    // constant var for api url
    public static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    public static final String TAG = "MainActivity";

    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvMovies = findViewById(R.id.rvMovies);
        movies = new ArrayList<>();

//      Create the adapter
        final MovieItemAdapter movieAdapter = new MovieItemAdapter(this, movies);
//      Set the adapter on the recycler view
        rvMovies.setAdapter(movieAdapter);
//      Set a layout manager on the recycler view
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        // Network Requests are Asynchronous -> Response Handler
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() { //Movie DB Communicates w/ JSON< so we utilize this handler
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d("TAG", "onSuccess, Connection established");
                // Try catch for JSONObject (Array passes data)
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Results: "+ results.toString());
                    Log.i(TAG, "Movies: "+ movies.size());
                } catch (JSONException e) {
//                    e.printStackTrace();
                    Log.e(TAG, "Hit json exception", e);
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure, Connection not established");
            }
        });
    }
}