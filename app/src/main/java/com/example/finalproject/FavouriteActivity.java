package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.finalproject.Adapters.FavouriteRecipeAdapter;
import com.example.finalproject.Adapters.UserRecipeAdapter;
import com.example.finalproject.Listeners.RecipeClickListener;
import com.example.finalproject.Models.FavouriteRecipe;
import com.example.finalproject.Models.Recipe;
import com.example.finalproject.Models.UserIngredient;
import com.example.finalproject.Models.UserRecipe;
import com.example.finalproject.Storage.SystemStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {
    ImageView addNewRecipeFromFav;
    Button goToUploadedFromFav;
    Button goToFeedFromFav;

    ArrayList<Recipe> dataSet;

    RecyclerView recycleView;
    LinearLayoutManager layoutManager;
    FavouriteRecipeAdapter adapter;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        addNewRecipeFromFav = findViewById(R.id.addNewRecipeFromFav);
        goToUploadedFromFav = findViewById(R.id.goToUploadedFromFav);
        goToFeedFromFav = findViewById(R.id.goToFeedFromFav);

        addNewRecipeFromFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FavouriteActivity.this, addRecipeActivity.class);

                FavouriteActivity.this.startActivity(intent);
            }
        });

        goToUploadedFromFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FavouriteActivity.this, UploadedRecipesActivity.class);

                FavouriteActivity.this.startActivity(intent);
            }
        });

        goToFeedFromFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FavouriteActivity.this, FeedActivity.class);

                FavouriteActivity.this.startActivity(intent);
            }
        });

        recycleView = findViewById(R.id.recycler_favourite_recipes);
        layoutManager = new LinearLayoutManager(this); // new GridLayoutManager
        dataSet = new ArrayList<Recipe>();
        database = FirebaseDatabase.getInstance().getReference("favourite_recipes");

        recycleView.setLayoutManager(layoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());

        adapter = new FavouriteRecipeAdapter(this, dataSet, recipeClickListener);
        recycleView.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<FavouriteRecipe> favouriteRecipes = new ArrayList<>();

                for (DataSnapshot recipeSnapshot : snapshot.getChildren()) {
                    FavouriteRecipe favouriteRecipe = new FavouriteRecipe(recipeSnapshot.child("recipe").getValue(Recipe.class), String.valueOf(recipeSnapshot.child("uid").getValue()));
                    favouriteRecipes.add(favouriteRecipe);
                }

                for (FavouriteRecipe favouriteRecipe : favouriteRecipes)
                    if (favouriteRecipe.uid.equals(SystemStorage.getCurrentUID()))
                        dataSet.add(favouriteRecipe.recipe);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(FavouriteActivity.this, RecipeDetailsActivity.class).putExtra("id", id));
        }
    };
}