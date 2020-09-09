package com.samir.spotifyapi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samir.spotifyapi.R;
import com.samir.spotifyapi.classes.Tracks;

import java.util.ArrayList;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.MyViewHolder> {
    private ArrayList<Tracks> arrayListTracks;

    public TrackAdapter(ArrayList<Tracks> arrayList){
        this.arrayListTracks = arrayList;
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
