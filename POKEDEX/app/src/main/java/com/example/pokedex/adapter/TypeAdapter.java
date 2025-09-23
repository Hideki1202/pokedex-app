package com.example.pokedex.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokedex.R;
import com.example.pokedex.api.RetrofitClient;
import com.example.pokedex.api.dto.PokemonDetailsResponse;
import com.example.pokedex.api.service.PokeApiService;
import com.example.pokedex.model.Pokemon;
import com.example.pokedex.ui.PokemonDetails;

import java.util.List;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.TypeViewHolder> {

    private List<String> types;

    public TypeAdapter(List<String> types) {
        this.types = types;
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_type_tag, parent, false);
        return new TypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, int position) {
        String type = types.get(position);
        holder.tvType.setText(type);

        int color = getColorForType(type, holder.itemView.getContext());

        // cria um shape arredondado dinamicamente
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(color);
        bg.setCornerRadius(24f); // arredondado
        holder.tvType.setBackground(bg);
    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    static class TypeViewHolder extends RecyclerView.ViewHolder {
        TextView tvType;
        public TypeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_type_tag);
        }
    }

    private int getColorForType(String type, Context ctx) {
        String normalized = type == null ? "" : type.trim().toLowerCase();
        switch (normalized) {
            case "fire": return Color.parseColor("#F08030");
            case "water": return Color.parseColor("#6890F0");
            case "grass": return Color.parseColor("#78C850");
            case "electric": return Color.parseColor("#F8D030");
            case "bug": return Color.parseColor("#A8B820");
            case "normal": return Color.parseColor("#A8A878");
            case "poison": return Color.parseColor("#D15DFF");
            case "ground": return Color.parseColor("#E0C068");
            case "flying": return Color.parseColor("#9ADDFF");
            case "psychic": return Color.parseColor("#F85888");
            case "rock": return Color.parseColor("#B8A038");
            case "ice": return Color.parseColor("#98D8D8");
            case "dragon": return Color.parseColor("#7038F8");
            case "dark": return Color.parseColor("#705848");
            case "steel": return Color.parseColor("#B8B8D0");
            case "fairy": return Color.parseColor("#F0B6BC");
            default: return Color.GRAY;
        }
    }

}
