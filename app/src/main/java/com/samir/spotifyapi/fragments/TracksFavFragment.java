package com.samir.spotifyapi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samir.spotifyapi.R;
import com.samir.spotifyapi.adapters.TrackAdapter;
import com.samir.spotifyapi.classes.InternalTracks;

import static com.samir.spotifyapi.fragments.TracksFragment.recyclerViewTrackFav;


public class TracksFavFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracks_fav, container, false);

        recyclerViewTrackFav = view.findViewById(R.id.recyclerTracksFav);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewTrackFav.setLayoutManager(layoutManager);
        recyclerViewTrackFav.setHasFixedSize(true);
        recyclerViewTrackFav.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        InternalTracks internalTracks = new InternalTracks(getActivity());
        TrackAdapter trackAdapter = new TrackAdapter(internalTracks.getListInternalTracks(), getActivity());
        recyclerViewTrackFav.setAdapter(trackAdapter);
    }

}