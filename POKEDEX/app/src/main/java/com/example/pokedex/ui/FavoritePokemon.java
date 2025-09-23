package com.example.pokedex.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

import com.example.pokedex.MainActivity;
import com.example.pokedex.R;
import com.example.pokedex.adapter.PokemonAdapter;
import com.example.pokedex.api.RetrofitClient;
import com.example.pokedex.api.dto.PokemonResponse;
import com.example.pokedex.api.service.PokeApiService;
import com.example.pokedex.model.Pokemon;
import com.example.pokedex.utils.FavoriteUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritePokemon extends AppCompatActivity {

    RecyclerView recyclerView;
    PokemonAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_pokemon);

        recyclerView = findViewById(R.id.recyclerViewPokemonsFavs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Pokemon> favoritos = FavoriteUtils.getFavorites(this);
        TextView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        TextView btnBox = findViewById(R.id.btnBox);
        btnBox.setOnClickListener(v -> {
            Intent intent = new Intent(this, BoxActivity.class);
            startActivity(intent);
        });

        adapter = new PokemonAdapter(favoritos, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);


    }


}