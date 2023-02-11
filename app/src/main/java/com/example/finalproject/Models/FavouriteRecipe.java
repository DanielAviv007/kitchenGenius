package com.example.finalproject.Models;

public class FavouriteRecipe {
    public Recipe recipe;
    public String uid;

    public FavouriteRecipe(Recipe recipe, String uid) {
        this.recipe = recipe;
        this.uid = uid;
    }
}
