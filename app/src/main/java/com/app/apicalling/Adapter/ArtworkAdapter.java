package com.app.apicalling.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.apicalling.Model.Artworks;
import com.app.apicalling.R;

import java.util.List;

public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkAdapter.MyViewHolder> {
    public List<Artworks> allArtworks;
    public Context context;

    public ArtworkAdapter(Context context, List<Artworks> allArtworks) {
        this.context = context;
        this.allArtworks = allArtworks;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Artworks currentItem = allArtworks.get(position);
        holder.tv_title.setText(currentItem.getTitle());
        holder.tv_fiscal_year.setText(String.valueOf(currentItem.getFiscal_year()));
        holder.tv_artist_id.setText(String.valueOf(currentItem.getArtist_id()));
        holder.tv_artist_display.setText(currentItem.getArtist_display());
    }

    @Override
    public int getItemCount() {
        return allArtworks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_artist_id, tv_fiscal_year, tv_artist_display, tv_title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_artist_id = itemView.findViewById(R.id.tv_artist_id);
            tv_artist_display = itemView.findViewById(R.id.tv_artist_display);
            tv_fiscal_year = itemView.findViewById(R.id.tv_fiscal_year);
            tv_title = itemView.findViewById(R.id.tv_title);

        }
    }

}
