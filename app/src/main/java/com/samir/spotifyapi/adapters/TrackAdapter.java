package com.samir.spotifyapi.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samir.spotifyapi.R;
import com.samir.spotifyapi.activities.TrackDetailsActivity;
import com.samir.spotifyapi.classes.Tracks;

import java.util.ArrayList;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.MyViewHolder> {
    private ArrayList<Tracks> arrayListTracks;
    private Context context;

    public TrackAdapter(ArrayList<Tracks> arrayList, Context context){
        this.arrayListTracks = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_adapter, parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Tracks tracks = arrayListTracks.get(position);
        holder.tvMusic.setText(tracks.getMusicName());
        holder.tvArt.setText(tracks.getArtistName());
        holder.itemView.setOnClickListener(c -> {
            Intent intent = new Intent(context, TrackDetailsActivity.class);
            intent.putExtra("object", tracks);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return arrayListTracks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvMusic, tvArt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMusic = itemView.findViewById(R.id.tvMusicName);
            tvArt = itemView.findViewById(R.id.tvArtName);
        }
    }

}
