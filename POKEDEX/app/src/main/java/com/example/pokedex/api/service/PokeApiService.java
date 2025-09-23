package com.example.pokedex.api.service;

import com.example.pokedex.api.dto.PokemonDetailsResponse;
import com.example.pokedex.api.dto.PokemonResponse;
import com.example.pokedex.model.Pokemon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface PokeApiService {

    @GET("pokemon")
    Call<PokemonResponse> getPokemons(@Query("limit") int limit, @Query("offset") int offset);
    @GET
    Call<PokemonDetailsResponse> getPokemonByUrl(@Url String url);

}


