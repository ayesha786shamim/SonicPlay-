package com.example.musicapplication.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.musicapplication.Model.Song;
import com.example.musicapplication.R;
import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private ArrayList<Song> songs;
    private OnSongClickListener onSongClickListener;

    public interface OnSongClickListener {
        void onClick(Song song, ArrayList<Song> songList);
    }

    public SongAdapter(ArrayList<Song> songs, OnSongClickListener onSongClickListener) {
        this.songs = songs;
        this.onSongClickListener = onSongClickListener;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item_song.xml layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.titleTextView.setText(song.getTitle());

        holder.itemView.setOnClickListener(v -> onSongClickListener.onClick(song,songs));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    // Method to update the song list in the adapter
    public void updateSongs(ArrayList<Song> newSongs) {
        this.songs.clear();  // Clear the current list
        this.songs.addAll(newSongs);  // Add the new list of songs
        notifyDataSetChanged();  // Notify the adapter that the data has changed
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;

        public SongViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.song_title);
        }
    }
}