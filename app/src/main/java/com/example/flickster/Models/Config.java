package com.example.flickster.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    // The base url for loading images and the poster size for the image
    String imageBaseURL;
    String posterSize; // the postersize of the images, part of the url


    public Config(JSONObject object) throws JSONException {
        // Get the JSON object containign the informations we need
        JSONObject images = object.getJSONObject("images");
        //get the image url
        imageBaseURL = images.getString("secure_base_url");
        //Get the poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        posterSize = posterSizeOptions.optString(3, "w342");
    }

    //Helper method to generate imageURL
    public String getImageURL(String size, String path){
        return String.format("%s%s%s", imageBaseURL, size, path); //Just concatenate all three

    }

    public String getImageBaseURL() {
        return imageBaseURL;
    }

    public String getPosterSize() {
        return posterSize;
    }
}
