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
import com.samir.spotifyapi.activities.AlbDetailsActivity;
import com.samir.spotifyapi.activities.TrackDetailsActivity;
import com.samir.spotifyapi.classes.Albums;
import com.samir.spotifyapi.classes.Artists;
import com.samir.spotifyapi.classes.Tracks;

import java.util.ArrayList;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder> {
    private ArrayList<Albums> albumsArrayList;
    private Context context;

    public AlbumsAdapter(ArrayList<Albums> albumsArrayList, Context context) {
        this.albumsArrayList = albumsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_adapter, parent,false);
        return new AlbumsAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Albums albums = albumsArrayList.get(position);
        holder.textView.setText(albums.getAlbumName());

        Uri uriimg = Uri.parse(albums.getUrlImgSmall());
        Glide.with(context)
                .load(uriimg)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(c -> {
            Intent intent = new Intent(context, AlbDetailsActivity.class);
            intent.putExtra("album", albums);
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (Activity) context,
                    holder.imageView,
                    ViewCompat.getTransitionName(holder.imageView));
            context.startActivity(intent, activityOptionsCompat.toBundle());
        });

    }

    @Override
    public int getItemCount() {
        return albumsArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewAlbSmall);
            textView = itemView.findViewById(R.id.tvAlbName);
        }
    }

}
