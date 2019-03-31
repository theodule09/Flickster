package com.example.flickster.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
    // The base url for loading images and the poster size for the image
    String imageBaseURL;
    String posterSize; // the postersize of the images, part of the url
    //backdrop size for landscape mode
    String backdropSize;


    public Config(JSONObject object) throws JSONException {
        // Get the JSON object containign the informations we need
        JSONObject images = object.getJSONObject("images");
        //get the image url
        imageBaseURL = images.getString("secure_base_url");
        //Get the poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        posterSize = posterSizeOptions.optString(3, "w342");

        //Parse the backdrop size  or use w780 as fallback
        JSONArray backropsizeOption = images.getJSONArray("backdrop_sizes");
        backdropSize = backropsizeOption.optString(1, "w780");
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

    public String getBackdropSize() {
        return backdropSize;
    }
}
