package com.samir.spotifyapi.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.samir.spotifyapi.R;
import com.samir.spotifyapi.activities.ArtDetailsActivity;
import com.samir.spotifyapi.activities.TrackDetailsActivity;
import com.samir.spotifyapi.classes.Artists;
import com.samir.spotifyapi.classes.Tracks;

import java.util.ArrayList;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MyViewHolder> {
    private ArrayList<Artists> arrayListArtists;
    private Context context;

    public ArtistAdapter(ArrayList<Artists> arrayListArtists, Context context) {
        this.arrayListArtists = arrayListArtists;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_adapter, parent,false);
        return new ArtistAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Artists artists = arrayListArtists.get(position);
        holder.tvArtName.setText(artists.getArtName());

        Uri uriimg = Uri.parse(artists.getUrlImgArtSmall());
        Glide.with(context)
                .load(uriimg)
                .into(holder.imgSmall);

        holder.itemView.setOnClickListener(c -> {
            Intent intent = new Intent(context, ArtDetailsActivity.class);
            intent.putExtra("artist", artists);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (Activity) context,
                    holder.imgSmall,
                    ViewCompat.getTransitionName(holder.imgSmall));
            context.startActivity(intent, activityOptionsCompat.toBundle());
//            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return arrayListArtists.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvArtName;
        ImageView imgSmall;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvArtName = itemView.findViewById(R.id.tvArtName);
            imgSmall = itemView.findViewById(R.id.imageViewArtSmall);
        }
    }

}
