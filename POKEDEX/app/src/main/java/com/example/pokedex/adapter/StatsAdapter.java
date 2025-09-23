package com.example.pokedex.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.R;
import com.example.pokedex.api.dto.PokemonDetailsResponse;

import java.util.List;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder> {

    private List<PokemonDetailsResponse.StatSlot> stats;

    public StatsAdapter(List<PokemonDetailsResponse.StatSlot> stats) {
        this.stats = stats;
    }

    @NonNull
    @Override
    public StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stat, parent, false);
        return new StatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        PokemonDetailsResponse.StatSlot statSlot = stats.get(position);

        String statName = statSlot.getStat().getName().toUpperCase();
        int value = statSlot.getBaseStat();

        holder.tvStat.setText(statName + ": " + value);

        holder.tvStat.setBackgroundColor(android.graphics.Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    static class StatsViewHolder extends RecyclerView.ViewHolder {
        TextView tvStat;

        public StatsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStat = itemView.findViewById(R.id.tv_stat);
        }
    }
}