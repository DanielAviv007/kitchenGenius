package com.example.finalproject.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserRecipe {
    private String uid;
    private String title;
    private String photoPath;
    private ArrayList<UserIngredient> ingredients;
    private int servings;
    private int timeInMinutes;
    private String instructions;

    public UserRecipe(String uid, String title, String photoPath, ArrayList<UserIngredient> ingredients, int servings, int timeInMinutes, String instructions) {
        this.uid = uid;
        this.title = title;
        this.photoPath = photoPath;
        this.ingredients = ingredients;
        this.servings = servings;
        this.timeInMinutes = timeInMinutes;
        this.instructions = instructions;

        uploadToDataBase();
    }

    private void uploadToDataBase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipes").child(this.uid + this.title);

        myRef.setValue(this);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public ArrayList<UserIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<UserIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getTimeInMinutes() {
        return timeInMinutes;
    }

    public void setTimeInMinutes(int timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
