package com.example.pokedex.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokedex.R;
import com.example.pokedex.api.RetrofitClient;
import com.example.pokedex.api.dto.PokemonDetailsResponse;
import com.example.pokedex.api.service.PokeApiService;
import com.example.pokedex.model.Pokemon;
import com.example.pokedex.ui.PokemonDetails;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {


    private List<Pokemon> pokemonList;
    private Context context;
    private PokeApiService service;

    public PokemonAdapter(List<Pokemon> pokemonList, Context context) {
        this.pokemonList = pokemonList;
        this.context = context;
        this.service = RetrofitClient.getClient().create(PokeApiService.class);
    }

    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPokemon;
        TextView txtName, txtHeight, txtWeight;
        Button btnDetails;
        LinearLayout extraInfo;
        RecyclerView rvTypes; // recycler para os tipos

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPokemon = itemView.findViewById(R.id.pokemon_image);
            txtName = itemView.findViewById(R.id.pokemon_name);
            txtHeight = itemView.findViewById(R.id.pokemon_height);
            txtWeight = itemView.findViewById(R.id.pokemon_weight);
            extraInfo = itemView.findViewById(R.id.extra_info);
            btnDetails = itemView.findViewById(R.id.buttonPokemon);

            rvTypes = itemView.findViewById(R.id.rv_types);
            rvTypes.setLayoutManager(new LinearLayoutManager(
                    itemView.getContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false));
        }
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pokemon_card, parent, false);
        return new PokemonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.txtName.setText(pokemon.getName());
        Glide.with(holder.itemView.getContext())
                .load(pokemon.getImageUrl())
                .into(holder.imgPokemon);

        holder.extraInfo.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> {
            if (holder.extraInfo.getVisibility() == View.GONE) {
                Call<PokemonDetailsResponse> call = service.getPokemonByUrl(pokemon.getUrl());
                call.enqueue(new Callback<PokemonDetailsResponse>() {
                    @Override
                    public void onResponse(Call<PokemonDetailsResponse> call, Response<PokemonDetailsResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            PokemonDetailsResponse details = response.body();
                            holder.txtHeight.setText("Altura: " + details.getHeight());
                            holder.txtWeight.setText("Peso: " + details.getWeight());

                            // monta a lista de tipos
                            List<String> types = Arrays.stream(details.getTypesAsString().split(",")).toList();
                            TypeAdapter typeAdapter = new TypeAdapter(types);
                            holder.rvTypes.setAdapter(typeAdapter);

                            holder.extraInfo.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(context, "Erro ao carregar detalhes", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PokemonDetailsResponse> call, Throwable t) {
                        Toast.makeText(context, "Falha: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                holder.extraInfo.setVisibility(View.GONE);
            }
        });

        holder.btnDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, PokemonDetails.class);
            intent.putExtra("pokemon_url", pokemon.getUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }
}