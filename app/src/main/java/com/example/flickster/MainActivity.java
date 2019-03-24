package com.example.flickster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    // The base url for loading images and the poster size for the image
    String imageBaseURL;
    String posterSize;
   // ArrayList<Movie> movies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialize the client
        client = new AsyncHttpClient();
        getConfiguration();
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
                    // Get the JSON object containign the informations we need
                    JSONObject images = response.getJSONObject("images");
                    //get the image url
                    imageBaseURL = images.getString("secure_base_url");
                    //Get the poster size
                    JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
                    posterSize = posterSizeOptions.optString(3, "w342");
                    Toast.makeText(getApplicationContext(),"success parsing the JSON", Toast.LENGTH_LONG).show();
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
