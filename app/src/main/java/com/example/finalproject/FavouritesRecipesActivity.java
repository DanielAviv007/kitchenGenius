package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finalproject.Adapters.IngredientsAdapter;
import com.example.finalproject.Adapters.RecipeAdapter;
import com.example.finalproject.Listeners.RandomRecipeResponseListener;
import com.example.finalproject.Listeners.RecipeClickListener;
import com.example.finalproject.Listeners.RecipeDetailsListener;
import com.example.finalproject.Listeners.RecipeFavouriteListener;
import com.example.finalproject.Models.RandomRecipeApiResponse;
import com.example.finalproject.Models.Recipe;
import com.example.finalproject.Models.RecipeDetailsResponse;
import com.example.finalproject.Models.RecipeFavouriteResponse;
import com.example.finalproject.Storage.SystemStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavouritesRecipesActivity extends AppCompatActivity {
    private Button goToUploadedFromFav;
    private Button goToFeedFromFav;
    private ImageView addNewRecipeFromFav;

    private ArrayList<Recipe> dataSet;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecipeAdapter adapter;
    private DatabaseReference database;

    RequestManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites_recipes);

        manager = new RequestManager(this);

        layoutManager = new LinearLayoutManager(this); // new GridLayoutManager
        dataSet = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference("favourite_recipes");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> favRecipesIDs = new ArrayList<>();

                for (DataSnapshot recipeSnapshot : snapshot.getChildren())
                    if (recipeSnapshot.child("uid").getValue().toString().equals(SystemStorage.getCurrentUID()))
                        favRecipesIDs.add(recipeSnapshot.child("recipeID").getValue().toString());

                manager.getFavouriteRecipes(recipeFavouriteListener, favRecipesIDs);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        goToUploadedFromFav = findViewById(R.id.goToUploadedFromFav);
        goToUploadedFromFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FavouritesRecipesActivity.this, UploadedRecipesActivity.class);

                FavouritesRecipesActivity.this.startActivity(intent);
            }
        });

        addNewRecipeFromFav = findViewById(R.id.addNewRecipeFromFav);
        addNewRecipeFromFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FavouritesRecipesActivity.this, addRecipeActivity.class);
                FavouritesRecipesActivity.this.startActivity(intent);
            }
        });

        goToFeedFromFav = findViewById(R.id.goToFeedFromFav);
        goToFeedFromFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FavouritesRecipesActivity.this, FeedActivity.class);
                FavouritesRecipesActivity.this.startActivity(intent);
            }
        });
    }

    private final RecipeFavouriteListener recipeFavouriteListener = new RecipeFavouriteListener() {
        @Override
        public void didFetch(RecipeFavouriteResponse response, String message) {
            recyclerView = findViewById(R.id.recycler_favourites_recipes);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(FavouritesRecipesActivity.this, 1));
            adapter = new RecipeAdapter(FavouritesRecipesActivity.this, response.recipes, recipeClickListener);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void didError(String message) { }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(FavouritesRecipesActivity.this, RecipeDetailsActivity.class).putExtra("id", id));
        }
    };

}