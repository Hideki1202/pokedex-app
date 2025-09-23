package com.example.pokedex;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pokedex.adapter.PokemonAdapter;
import com.example.pokedex.api.RetrofitClient;
import com.example.pokedex.api.dto.PokemonResponse;
import com.example.pokedex.api.service.PokeApiService;
import com.example.pokedex.model.Pokemon;
import com.example.pokedex.ui.FavoritePokemon;
import com.example.pokedex.ui.PokemonDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PokemonAdapter adapter;
    Button pesquisarButton, anteriorButton, proximoButton;
    EditText pesquisarInput;

    List<Pokemon> pokemons = new ArrayList<>();
    int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NestedScrollView nestedScrollView = findViewById(R.id.scrollView);

        recyclerView = findViewById(R.id.recyclerViewPokemons);
        pesquisarButton = findViewById(R.id.search_btn);
        pesquisarInput = findViewById(R.id.search_input);
        anteriorButton = findViewById(R.id.buttonAnterior);
        proximoButton = findViewById(R.id.buttonProximo);
        ImageView favoritesButton = findViewById(R.id.favoritesButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PokemonAdapter(pokemons, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);

        proximoButton.setOnClickListener(v -> {
            offset += 20;
            carregarPokemons(offset);
            nestedScrollView.smoothScrollTo(0, 0);

        });
        anteriorButton.setOnClickListener(v->{
            offset -= 20;
            carregarPokemons(offset);
            nestedScrollView.smoothScrollTo(0, 0);

        });
        favoritesButton.setOnClickListener( v->{
            Intent intent = new Intent(this, FavoritePokemon.class);
            startActivity(intent);
        });
        carregarPokemons(0);
    }

    private void carregarPokemons(int offset) {
        PokeApiService service = RetrofitClient.getClient().create(PokeApiService.class);
        Call<PokemonResponse> call = service.getPokemons(20, offset);

        call.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful()) {
                    List<Pokemon> results = response.body().getResults();
                    pokemons.clear();
                    for (Pokemon result : results) {



                        pokemons.add(new Pokemon( capitalize(result.getName()), "Pokémon", result.getUrl()));

                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Erro na resposta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Falha: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        pesquisarButton.setOnClickListener(v -> {

            Intent intent = new Intent(this, PokemonDetails.class);
            intent.putExtra("pokemon_url",  "https://pokeapi.co/api/v2/pokemon/"+pesquisarInput.getText()); // passa URL
            startActivity(intent);
        });
    }

    private String capitalize(String name) {
        if (name == null || name.isEmpty()) return name;
        return name.substring(0,1).toUpperCase() + name.substring(1);
    }
}
