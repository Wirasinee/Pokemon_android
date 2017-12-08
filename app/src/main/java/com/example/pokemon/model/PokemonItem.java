package com.example.pokemon.model;

/**
 * Created by Wirasinee on 08-Dec-17.
 */

public class PokemonItem {
    //กรณีclassไม่ได้มีการแก้ไขก็เป็นfinal แล้วเป็นpublic ไปเลย
    public final int id;
    public final String title;
    public final String name;
    public final String picture;

    //ALT+INS
    public PokemonItem(int id, String name, String title, String picture) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.picture = picture;
    }
    //ไปactivity_main MainAcitivity [3]


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    @Override
    public String toString() {
        return title;
    }
}
