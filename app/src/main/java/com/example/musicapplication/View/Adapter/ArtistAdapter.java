package com.example.musicapplication.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.musicapplication.R;
import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private List<String> artistList;
    private OnArtistClickListener onArtistClickListener;

    // Interface to handle click events on artists
    public interface OnArtistClickListener {
        void onArtistClick(String artistName);
    }

    public ArtistAdapter(List<String> artistList, OnArtistClickListener onArtistClickListener) {
        this.artistList = artistList;
        this.onArtistClickListener = onArtistClickListener;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        return new ArtistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        String artist = artistList.get(position);
        holder.artistTextView.setText(artist);

        holder.itemView.setOnClickListener(v -> onArtistClickListener.onArtistClick(artist));
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        public TextView artistTextView;

        public ArtistViewHolder(View view) {
            super(view);
            artistTextView = view.findViewById(R.id.artist_name);
        }
    }
}
