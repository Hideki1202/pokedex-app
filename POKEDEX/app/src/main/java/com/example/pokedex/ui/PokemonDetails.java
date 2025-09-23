package com.example.pokedex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pokedex.MainActivity;
import com.example.pokedex.R;
import com.example.pokedex.adapter.StatsAdapter;
import com.example.pokedex.adapter.TypeAdapter;
import com.example.pokedex.api.RetrofitClient;
import com.example.pokedex.api.dto.PokemonDetailsResponse;
import com.example.pokedex.api.service.PokeApiService;
import com.example.pokedex.model.Pokemon;
import com.example.pokedex.utils.FavoriteUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonDetails extends AppCompatActivity {

    TextView txtName, txtType, txtHeight, txtWeight, nextPokemon, previousPokemon, txtId, txtVoltar;
    String previousUrl;
    String nextUrl;
    ImageView imgPokemon;
    Button galleryBtn;
    ArrayList<String> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);
        txtVoltar = findViewById(R.id.voltar);
        txtName = findViewById(R.id.pokemon_name_details);
        imgPokemon = findViewById(R.id.pokemon_image_details);
        txtId = findViewById(R.id.pokemon_id_details);
        galleryBtn = findViewById(R.id.buttonGallery);
        nextPokemon = findViewById(R.id.next_pokemon);
        previousPokemon = findViewById(R.id.previous_pokemon);
        String pokemonUrl = getIntent().getStringExtra("pokemon_url");
        Button buttonFavorite = findViewById(R.id.buttonFavorite);
        RecyclerView recyclerStats = findViewById(R.id.rv_stats);
        RecyclerView recyclerTypes = findViewById(R.id.recyclerTypes);
        recyclerTypes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerStats.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));



        PokeApiService service = RetrofitClient.getClient().create(PokeApiService.class);
        Call<PokemonDetailsResponse> call = service.getPokemonByUrl(pokemonUrl);

        call.enqueue(new Callback<PokemonDetailsResponse>() {
            @Override
            public void onResponse(Call<PokemonDetailsResponse> call, Response<PokemonDetailsResponse> response) {
                if (response.isSuccessful()) {
                    PokemonDetailsResponse details = response.body();
                    txtName.setText(details.getName());
                    List<String> types = Arrays.stream(details.getTypesAsString().split(",")).toList();
                    TypeAdapter adapterTypes = new TypeAdapter(types);
                    StatsAdapter adapterStats = new StatsAdapter(details.getStats());
                    recyclerTypes.setAdapter(adapterTypes);
                    recyclerStats.setAdapter(adapterStats);

                    txtId.setText("Id: "+ details.getId());
                    nextPokemon.setText("Ver próximo pokémon: "+ (Integer.parseInt(details.getId())+1));
                    nextUrl = "https://pokeapi.co/api/v2/pokemon/" +(Integer.parseInt(details.getId())+1)+"/";

                    if (details.getId().equals("1")){
                        previousPokemon.setText("Não há pokemon anterior: ");
                        previousUrl = "https://pokeapi.co/api/v2/pokemon/" +1+"/";
                    }else {
                        previousPokemon.setText("Ver pokémon anterior: "+ (Integer.parseInt(details.getId())-1));
                        previousUrl = "https://pokeapi.co/api/v2/pokemon/" +(Integer.parseInt(details.getId())-1)+"/";
                    }


                    if (details.getSprites().getFrontDefault() != null)
                        images.add(details.getSprites().getFrontDefault());
                    if (details.getSprites().getBackDefault() != null)
                        images.add(details.getSprites().getBackDefault());
                    if (details.getSprites().getFrontShiny() != null)
                        images.add(details.getSprites().getFrontShiny());
                    if (details.getSprites().getBackShiny() != null)
                        images.add(details.getSprites().getBackShiny());

                    if (details.getSprites().getOther() != null &&
                            details.getSprites().getOther().getOfficialArtwork() != null &&
                            details.getSprites().getOther().getOfficialArtwork().getFrontDefault() != null) {
                        images.add(details.getSprites().getOther().getOfficialArtwork().getFrontDefault());
                    }

                    List<Pokemon> favoritos = FavoriteUtils.getFavorites(PokemonDetails.this);
                    boolean isFavorito = favoritos.stream().anyMatch(p -> p.getId() == Integer.parseInt(details.getId()));

                    buttonFavorite.setText(isFavorito ? "Desfavoritar" : "Adicionar aos favoritos");
                    String name = details.getName();
                    String type = details.getTypesAsString(); // pode ser "fire, flying" etc.
                    String url = "https://pokeapi.co/api/v2/pokemon/" + details.getId() + "/";

                    Pokemon pokemon = new Pokemon(name, type, url);

                    buttonFavorite.setOnClickListener(v -> {

                        if (isFavorito) {
                            FavoriteUtils.removeFavorite(PokemonDetails.this,  Integer.parseInt(details.getId()));
                            buttonFavorite.setText("Favoritar");
                            Toast.makeText(PokemonDetails.this, "Pokémon removido dos favoritos!", Toast.LENGTH_SHORT).show();
                        } else {
                            FavoriteUtils.addFavorite(PokemonDetails.this,  pokemon);
                            buttonFavorite.setText("Desfavoritar");
                            Toast.makeText(PokemonDetails.this, "Pokémon adicionado aos favoritos!", Toast.LENGTH_SHORT).show();
                        }
                    });



                    Glide.with(PokemonDetails.this)
                            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" +  details.getId()+ ".png") // exemplo
                            .into(imgPokemon);
                } else {
                    Toast.makeText(PokemonDetails.this, "Erro ao carregar detalhes", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<PokemonDetailsResponse> call, Throwable t) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                Toast.makeText(PokemonDetails.this, "Falha: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        nextPokemon.setOnClickListener(v -> {
            Intent intent = new Intent(this, PokemonDetails.class);
            intent.putExtra("pokemon_url", nextUrl); // passa URL
            startActivity(intent);
        });
        previousPokemon.setOnClickListener(v -> {
            Intent intent = new Intent(this, PokemonDetails.class);
            intent.putExtra("pokemon_url", previousUrl); // passa URL
            startActivity(intent);
        });
        txtVoltar.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        galleryBtn.setOnClickListener(v ->{
            Intent intent = new Intent(this, GalleryActivity.class);
            intent.putStringArrayListExtra("pokemon_images", images);
            this.startActivity(intent);
        });



    }
}