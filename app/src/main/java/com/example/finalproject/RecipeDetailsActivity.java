package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.Adapters.IngredientsAdapter;
import com.example.finalproject.Listeners.RecipeDetailsListener;
import com.example.finalproject.Models.RecipeDetailsResponse;
import com.squareup.picasso.Picasso;

public class RecipeDetailsActivity extends AppCompatActivity {
    int id;
    TextView textView_dish_name;
    TextView textView_dish_source;
    TextView textView_dish_summary;
    ImageView imageView_dish_image;
    RecyclerView recycler_dish_ingredients;
    RequestManager manager;
    ProgressDialog dialog;
    IngredientsAdapter ingredientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        findViews();

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        manager = new RequestManager(this);
        manager.getRecipeDetails(recipeDetailsListener, id);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Details...");
        dialog.show();
    }

    private void findViews() {
        textView_dish_name = findViewById(R.id.textView_dish_name);
        textView_dish_source = findViewById(R.id.textView_dish_source);
        textView_dish_summary = findViewById(R.id.textView_dish_summary);
        imageView_dish_image = findViewById(R.id.imageView_dish_image);
        recycler_dish_ingredients = findViewById(R.id.recycler_dish_ingredients);
    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            dialog.dismiss();
            textView_dish_name.setText(response.title);
            textView_dish_source.setText(response.sourceName);
            textView_dish_summary.setText(response.summary);
            Picasso.get().load(response.image).into(imageView_dish_image);

            recycler_dish_ingredients.setHasFixedSize(true);
            recycler_dish_ingredients.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            ingredientsAdapter = new IngredientsAdapter(RecipeDetailsActivity.this, response.extendedIngredients);
            recycler_dish_ingredients.setAdapter(ingredientsAdapter);

        }

        @Override
        public void didError(String message) {
            Toast.makeText(RecipeDetailsActivity.this, message, Toast.LENGTH_SHORT).show();

        }
    };
}