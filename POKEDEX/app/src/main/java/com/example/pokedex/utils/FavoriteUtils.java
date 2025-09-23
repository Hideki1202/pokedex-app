package com.example.pokedex.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.pokedex.model.Pokemon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoriteUtils {
    private static final String PREFS_NAME = "favorites_prefs";
    private static final String KEY_FAVORITES = "favorites_list";

    public static void addFavorite(Context context, Pokemon pokemon) {
        List<Pokemon> favorites = getFavorites(context);
        // Evitar duplicatas
        boolean exists = false;
        for (Pokemon p : favorites) {
            if (p.getId() == pokemon.getId()) {
                exists = true;
                break;
            }
        }
        if (!exists) favorites.add(pokemon);

        saveFavorites(context, favorites);
    }

    public static void removeFavorite(Context context, int pokemonId) {
        List<Pokemon> favorites = getFavorites(context);
        favorites.removeIf(p -> p.getId() == pokemonId);
        saveFavorites(context, favorites);
    }

    public static List<Pokemon> getFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_FAVORITES, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<Pokemon>>(){}.getType();
        return new Gson().fromJson(json, type);
    }

    private static void saveFavorites(Context context, List<Pokemon> favorites) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = new Gson().toJson(favorites);
        prefs.edit().putString(KEY_FAVORITES, json).apply();
    }
    public static List<String> getFavoriteImageUrls(Context context) {
        List<Pokemon> favorites = getFavorites(context);
        List<String> imageUrls = new ArrayList<>();
        for (Pokemon p : favorites) {
            // supondo que seu model tenha getImageUrl() ou getUrl()
            imageUrls.add(p.getImageUrl());
            // ou imageUrls.add(p.getUrl());  <-- use o campo certo
        }
        return imageUrls;
    }
}
