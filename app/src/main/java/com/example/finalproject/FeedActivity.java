package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.finalproject.Adapters.RecipeAdapter;
import com.example.finalproject.Listeners.RandomRecipeResponseListener;
import com.example.finalproject.Listeners.RecipeClickListener;
import com.example.finalproject.Models.RandomRecipeApiResponse;
import com.example.finalproject.Models.UserIngredient;
import com.example.finalproject.Models.UserRecipe;
import com.example.finalproject.Storage.SystemStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {
    RequestManager manager;
    RecipeAdapter recipeAdapter;
    RecyclerView recyclerView;
    Spinner spinner;
    List<String> tags = new ArrayList<>();
    SearchView searchView;
    ImageView addRecipe;

    Button goToUploaded;
    Button goToFavourites;
    ImageFilterView logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        searchView = findViewById(R.id.searchView_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tags.clear();
                tags.add(query);
                manager.getRandomRecipes(randomRecipeResponseListener,tags);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        addRecipe = findViewById(R.id.imageView_add_new_recipe);
        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FeedActivity.this, addRecipeActivity.class);
                FeedActivity.this.startActivity(intent);
            }
        });

        spinner = findViewById(R.id.spinner_tags);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.categories, R.layout.spinner_text);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(spinnerSelectedListener);

        manager = new RequestManager(this);

        goToUploaded = findViewById(R.id.goToUploaded);
        goToUploaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FeedActivity.this, UploadedRecipesActivity.class);

                FeedActivity.this.startActivity(intent);
            }
        });

        goToFavourites = findViewById(R.id.goToFavourites);
        goToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FeedActivity.this, FavouriteActivity.class);

                FeedActivity.this.startActivity(intent);
            }
        });

        logOut = findViewById(R.id.feedLogoutBtn);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FeedActivity.this, MainActivity.class);

                FeedActivity.this.startActivity(intent);
            }
        });
    }

    private final RandomRecipeResponseListener randomRecipeResponseListener = new RandomRecipeResponseListener() {
        @Override
        public void didFetch(RandomRecipeApiResponse response, String message) {
            recyclerView = findViewById(R.id.recycler_recipes);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(FeedActivity.this, 1));
            recipeAdapter = new RecipeAdapter(FeedActivity.this, response.recipes, recipeClickListener);
            recyclerView.setAdapter(recipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(FeedActivity.this, message, Toast.LENGTH_SHORT);
        }
    };
    private final AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            tags.clear();
            tags.add(adapterView.getSelectedItem().toString());
            manager.getRandomRecipes(randomRecipeResponseListener,tags);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            startActivity(new Intent(FeedActivity.this, RecipeDetailsActivity.class).putExtra("id", id));
        }
    };
}