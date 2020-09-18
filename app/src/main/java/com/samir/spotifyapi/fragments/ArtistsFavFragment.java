package com.samir.spotifyapi.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.samir.spotifyapi.R;
import com.samir.spotifyapi.adapters.ArtistAdapter;
import com.samir.spotifyapi.adapters.TrackAdapter;
import com.samir.spotifyapi.classes.InternalArtists;
import com.samir.spotifyapi.classes.InternalTracks;

import static com.samir.spotifyapi.fragments.TracksFragment.recyclerViewArtistsFav;


public class ArtistsFavFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists_fav, container, false);

        recyclerViewArtistsFav = view.findViewById(R.id.recyclerArtistsFav);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewArtistsFav.setLayoutManager(layoutManager);
        recyclerViewArtistsFav.setHasFixedSize(true);
        recyclerViewArtistsFav.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        InternalArtists internalArtists = new InternalArtists(getActivity());
        ArtistAdapter artistAdapter = new ArtistAdapter(internalArtists.getArtistsArrayList(), getActivity());
        recyclerViewArtistsFav.setAdapter(artistAdapter);

    }
}