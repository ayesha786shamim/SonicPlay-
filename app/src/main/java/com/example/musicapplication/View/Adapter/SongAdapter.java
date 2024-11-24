package com.example.musicapplication.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapplication.Model.Song;
import com.example.musicapplication.R;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Song> songList;
    private OnSongClickListener onSongClickListener;

    public interface OnSongClickListener {
        void onSongClick(Song song);
    }

    public SongAdapter(List<Song> songList, OnSongClickListener onSongClickListener) {
        this.songList = songList;
        this.onSongClickListener = onSongClickListener;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.songTitle.setText(song.getTitle());

        holder.itemView.setOnClickListener(v -> onSongClickListener.onSongClick(song));
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;

        public SongViewHolder(View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.song_title);
        }
    }
}




