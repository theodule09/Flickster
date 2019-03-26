package com.example.flickster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.flickster.Models.Config;
import com.example.flickster.Models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {


    //constants
    //The base URL for the API
    public static final String API_BASE_URL = "https://api.themoviedb.org/3";
    // The parameter for the API key
    public static final String API_KEY_PARAM = "api_key";

    // A tag for possible errors
    public final static String TAG ="Movie List Activity";

    //Instance Field
    AsyncHttpClient client;


    // list of the now playing movies
    ArrayList<Movie> movies;

    //The recycler view
    RecyclerView rvMovies;
    // The adapter wired to the rvmovies
    MovieAdapter adapter;

    //Image Config
    Config config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize the client
        client = new AsyncHttpClient();
        // Initialize the movie arraylist
        movies = new ArrayList<>();
        // initialize the adapter - movies array cannot be initialized after this point
        adapter = new MovieAdapter(movies);
        // resolve the recycle view
        rvMovies = findViewById(R.id.rvMovies);
        // connect a layoyt manager to the adapter
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        // Get the configuration on app creation
        getConfiguration();

    }

    private void getNowPlaying(){
        String url = API_BASE_URL + "/movie/now_playing";
        //Set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // THe API key is always requested
        //Execute a get reequest expecting a Json object
        client.get(url,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    // THe results in the response is an array, so we use the getJsonArray method
                    JSONArray results = response.getJSONArray("results");
                    // Iterate over the array and add the movie to the arraylist movies
                    for (int i = 0; i < results.length(); i++){
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        // notify the dataset change
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG, String.format("load %s movies", results.length()));

                } catch (JSONException e) {
                    logError("Failed to parse results from the now_playing", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               logError("Failed to get data from the now playing", throwable, true);
            }
        });
    }


    private void getConfiguration(){
        String url = API_BASE_URL + "/configuration";
        //Set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // THe API key is always requested
        //Execute a get reequest expecting a Json object
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    config = new Config(response);
                    Toast.makeText(getApplicationContext(),"success parsing the JSON", Toast.LENGTH_LONG).show();
                    Log.i(TAG, String.format("Loaded ImageBase URL %s and posterSize %s", config.getImageBaseURL(),config.getPosterSize()));
                    // Pass config to adapater
                    adapter.setConfig(config);

                    // Get the now playing movie list
                    getNowPlaying();

                } catch (JSONException e) {
                    logError("Failed parsing configuration",e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
               logError("Failed Getting configuration", throwable, true);
            }
        });
    }
    //handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser){
        //always log the error
        Log.e(TAG, message, error);
        if (alertUser){
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }

    }

}
