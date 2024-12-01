package com.example.musicapplication.View.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapplication.R;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private List<String> albumList;
    private OnAlbumClickListener onAlbumClickListener;

    // Interface to handle click events on albums
    public interface OnAlbumClickListener {
        void onAlbumClick(String albumName);
    }

    public AlbumAdapter(List<String> albumList, OnAlbumClickListener onAlbumClickListener) {
        this.albumList = albumList;
        this.onAlbumClickListener = onAlbumClickListener;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new AlbumViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        String album = albumList.get(position);
        holder.albumTextView.setText(album);

        holder.itemView.setOnClickListener(v -> onAlbumClickListener.onAlbumClick(album));
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {
        public TextView albumTextView;

        public AlbumViewHolder(View view) {
            super(view);
            albumTextView = view.findViewById(R.id.album_name);
        }
    }
}

