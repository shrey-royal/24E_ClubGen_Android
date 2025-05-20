package com.royal.music_player.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.royal.music_player.R;
import com.royal.music_player.dto.Song;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Song> songsList;

    public MainRecyclerViewAdapter(Context context, ArrayList<Song> songsList) {
        this.context = context;
        this.songsList = songsList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_song, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (songsList.get(position) != null) {
            holder.songId.setText("" + (position + 1));
            holder.title.setText(songsList.get(position).getTitle());
            if (songsList.get(position).getArtist() != null) {
                if (songsList.get(position).getArtist().trim().isEmpty()) {
                    songsList.get(position).setArtist("Unknown Artist");
                }
            }
            holder.artist.setText(songsList.get(position).getArtist());
            int duration = songsList.get(position).getDuration();
            holder.duration.setText(duration / 60 + ":" + (new DecimalFormat("00").format(duration % 60)));
        }
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, songId, artist, duration;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.tvTitle);
            songId = itemView.findViewById(R.id.songId);
            artist = itemView.findViewById(R.id.tvArtist);

            duration = itemView.findViewById(R.id.tvDuration);
        }
    }
}
