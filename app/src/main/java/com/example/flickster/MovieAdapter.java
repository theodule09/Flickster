package com.example.flickster;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flickster.Models.Config;
import com.example.flickster.Models.Movie;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    //List of movies
    ArrayList<Movie> movies;

    //Context for rendering
    Context context;

    //Config needed for image URL
    Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    // Create and inflate a new view
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // get the context from the parent (viewGroup) and create the inflater
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //Create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, viewGroup, false);
        // Return a new viewholder
        return new ViewHolder(movieView);
    }

    //Binds an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // get the movie at the specified position i
        Movie movie = movies.get(i);
        //populate the view with the movie data
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        //Determine the orientation of the device
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        // create url for poster image
        String imageURL = null;
        if (isPortrait){
            imageURL = config.getImageURL(config.getPosterSize(), movie.getPosterPath());

        }else{
            imageURL = config.getImageURL(config.getBackdropSize(), movie.getBackdropPath());
        }

        //Get the correct imageview and the correct placeholder based on the orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? viewHolder.ivPosterImage: viewHolder.ivBackdropImage;

        //load image with glide
        GlideApp.with(context)
                .load(imageURL)
                .placeholder(placeholderId)
                .transform(new RoundedCorners(25))
                .into(imageView);


    }

    //Return thr total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //Create the viewholder as a static inner class
    public static class ViewHolder extends RecyclerView.ViewHolder{

        // Track the view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPosterImage = itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = itemView.findViewById(R.id.ivBackdropImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);

        }
    }

}
