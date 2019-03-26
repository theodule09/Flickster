package com.example.flickster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

        // create url for poster image
        String imageURL = config.getImageURL(config.getPosterSize(), movie.getPosterPath());

        //load image with glide
        Glide.with(context)
                .load(imageURL)
                //.bitmapTransform(new RoundedCornersTransformation(context, 20, 0))
                //.transform(new RoundedCornersTransformation(25, 0))
                .into(viewHolder.ivPosterImage);


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
        TextView tvTitle;
        TextView tvOverview;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPosterImage = itemView.findViewById(R.id.ivPosterImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);

        }
    }

}
