package com.example.musicapplication.View.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.musicapplication.R;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private List<String> playlists;
    private OnPlaylistClickListener onPlaylistClickListener;

    public interface OnPlaylistClickListener {
        void onPlaylistClick(String playlistName);
    }

    public PlaylistAdapter(List<String> playlists, OnPlaylistClickListener onPlaylistClickListener ) {
        this.playlists = playlists;
        this.onPlaylistClickListener = onPlaylistClickListener;
    }

    @Override
    public PlaylistAdapter.PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistAdapter.PlaylistViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(PlaylistAdapter.PlaylistViewHolder holder, int position) {
        String playlist = playlists.get(position);
        holder.playlistTextView.setText(playlist);

        holder.itemView.setOnClickListener(v -> onPlaylistClickListener.onPlaylistClick(playlist));
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        public TextView playlistTextView;

        public PlaylistViewHolder(View view) {
            super(view);
            playlistTextView = view.findViewById(R.id.playlist_name);
        }
    }
}
