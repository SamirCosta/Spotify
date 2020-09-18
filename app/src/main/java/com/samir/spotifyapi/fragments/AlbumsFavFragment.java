package com.samir.spotifyapi.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.LinearLayout;

import com.samir.spotifyapi.R;
import com.samir.spotifyapi.adapters.AlbumsAdapter;
import com.samir.spotifyapi.adapters.TrackAdapter;
import com.samir.spotifyapi.classes.InternalAlbums;
import com.samir.spotifyapi.classes.InternalTracks;

import static com.samir.spotifyapi.fragments.TracksFragment.recyclerViewAlbumsFav;
import static com.samir.spotifyapi.fragments.TracksFragment.recyclerViewTrackFav;

public class AlbumsFavFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums_fav, container, false);

        recyclerViewAlbumsFav = view.findViewById(R.id.recyclerAlbumsFav);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAlbumsFav.setLayoutManager(layoutManager);
        recyclerViewAlbumsFav.setHasFixedSize(true);
        recyclerViewAlbumsFav.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        InternalAlbums internalAlbums = new InternalAlbums(getActivity());
        AlbumsAdapter albumsAdapter = new AlbumsAdapter(internalAlbums.getAlbumsArrayList(), getActivity());
        recyclerViewAlbumsFav.setAdapter(albumsAdapter);
    }

}