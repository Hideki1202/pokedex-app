package com.example.pokedex.api.dto;

import java.util.List;

public class PokemonDetailsResponse {
    private int height;
    private int weight;
    private List<TypeSlot> types;
    private List<StatSlot> stats;  // nova lista de stats
    private String name;
    private String id;
    private Sprites sprites;

    public List<TypeSlot> getTypes() {
        return types;
    }

    public Sprites getSprites() { return sprites; }

    public List<StatSlot> getStats() {
        return stats;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getHeight() { return height; }
    public int getWeight() { return weight; }

    // retorna os tipos como string: "Fire, Flying"
    public String getTypesAsString() {
        StringBuilder sb = new StringBuilder();
        for (TypeSlot t : types) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(t.getType().getName());
        }
        return sb.toString();
    }

    // retorna os stats como string: "HP: 45, Attack: 49"
    public String getStatsAsString() {
        StringBuilder sb = new StringBuilder();
        for (StatSlot s : stats) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(s.getStat().getName()).append(": ").append(s.getBaseStat());
        }
        return sb.toString();
    }

    // --- Classes internas ---

    public static class TypeSlot {
        private Type type;
        public Type getType() { return type; }
    }

    public static class Type {
        private String name;
        public String getName() { return name; }
    }

    public static class StatSlot {
        private int base_stat;  // valor do stat
        private Stat stat;      // info do stat (nome)

        public int getBaseStat() { return base_stat; }
        public Stat getStat() { return stat; }
    }

    public static class Stat {
        private String name;
        public String getName() { return name; }
    }
    public static class Sprites {
        private String front_default;
        private String back_default;
        private String front_shiny;
        private String back_shiny;
        private OtherSprites other; // para sprites como official-artwork

        // getters
        public String getFrontDefault() { return front_default; }
        public String getBackDefault() { return back_default; }
        public String getFrontShiny() { return front_shiny; }
        public String getBackShiny() { return back_shiny; }
        public OtherSprites getOther() { return other; }

        public static class OtherSprites {
            private OfficialArtwork official_artwork;
            public OfficialArtwork getOfficialArtwork() { return official_artwork; }

            public static class OfficialArtwork {
                private String front_default;
                public String getFrontDefault() { return front_default; }
            }
        }
    }
    public int getTotalStats() {
        int total = 0;
        if (stats != null) {
            for (StatSlot s : stats) {
                total += s.getBaseStat();
            }
        }
        return total;
    }

}
