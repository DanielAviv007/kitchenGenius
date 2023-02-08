package com.example.finalproject.Models;

public class FavouriteRecipe {
    private String uid;
    private String recipeID;

    public FavouriteRecipe(String uid, String recipeID) {
        this.uid = uid;
        this.recipeID = recipeID;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }
}
