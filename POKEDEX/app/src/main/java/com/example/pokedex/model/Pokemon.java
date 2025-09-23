package com.example.pokedex.model;

public class Pokemon {
    private int id;
    private String name;
    private String type;
    private String imageUrl;
    private String url;



    public Pokemon( String name, String type, String url) {
        this.name = name;
        String[] parts = url.split("/");
        String idString = parts[parts.length - 1].isEmpty() ?
                parts[parts.length - 2] : parts[parts.length - 1];
        this.url = url;
        this.id =  Integer.parseInt(idString);
        this.type = type;
        this.imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + idString + ".png";

    }

    public String getUrl() {
        return url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
