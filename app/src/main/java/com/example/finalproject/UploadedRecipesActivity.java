package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.finalproject.Adapters.UserRecipeAdapter;
import com.example.finalproject.Models.UserIngredient;
import com.example.finalproject.Models.UserRecipe;
import com.example.finalproject.Storage.SystemStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UploadedRecipesActivity extends AppCompatActivity {
    private ArrayList<UserRecipe> dataSet;

    private RecyclerView recycleView;
    private LinearLayoutManager layoutManager;
    private UserRecipeAdapter adapter;
    private DatabaseReference database;

    Button goToFeedFromMyRecipes;
    Button goToFavouritesFromMyRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_recipes);

        recycleView = findViewById(R.id.recycler_user_recipes);
        layoutManager = new LinearLayoutManager(this); // new GridLayoutManager
        dataSet = new ArrayList<UserRecipe>();
        database = FirebaseDatabase.getInstance().getReference("recipes");

        recycleView.setLayoutManager(layoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());

        adapter = new UserRecipeAdapter(dataSet);
        recycleView.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<UserRecipe> recipes = new ArrayList<>();

                for (DataSnapshot recipeSnapshot : snapshot.getChildren()) {
                    String instructions =  recipeSnapshot.child("instructions").getValue().toString();
                    String photoPath =  recipeSnapshot.child("photoPath").getValue().toString();
                    int servings = recipeSnapshot.child("servings").getValue(Integer.class);
                    int timeInMinutes = recipeSnapshot.child("timeInMinutes").getValue(Integer.class);
                    String title =  recipeSnapshot.child("title").getValue().toString();
                    String uid =  recipeSnapshot.child("uid").getValue().toString();

                    ArrayList<UserIngredient> ingredients = new ArrayList<>();

                    for (DataSnapshot ingredientSnapshot : recipeSnapshot.child("ingredients").getChildren()) {
                        ingredients.add(new UserIngredient(
                                ingredientSnapshot.child("name").getValue().toString(),
                                ingredientSnapshot.child("unit").getValue().toString(),
                                ingredientSnapshot.child("quantity").getValue(Double.class)));
                    }

                    recipes.add(new UserRecipe(uid, title, photoPath, ingredients, servings, timeInMinutes, instructions));
                }

                for (UserRecipe userRecipe : recipes) {
                    if (userRecipe.getUid().equals(SystemStorage.getCurrentUID()))
                        dataSet.add(userRecipe);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        goToFeedFromMyRecipes = findViewById(R.id.goToFeedFromMyRecipes);
        goToFeedFromMyRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UploadedRecipesActivity.this, FeedActivity.class);

                UploadedRecipesActivity.this.startActivity(intent);
            }
        });
        goToFavouritesFromMyRecipes = findViewById(R.id.goToFavouritesFromMyRecipes);
        goToFavouritesFromMyRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UploadedRecipesActivity.this, FavouriteActivity.class);

                UploadedRecipesActivity.this.startActivity(intent);
            }
        });
    }
}