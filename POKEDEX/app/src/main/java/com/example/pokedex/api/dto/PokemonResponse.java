package com.example.pokedex.api.dto;

import com.example.pokedex.model.Pokemon;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PokemonResponse {
    @SerializedName("results")
    private List<Pokemon> results;

    public List<Pokemon> getResults() {
        return results;
    }

}
