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
import com.example.finalproject.Adapters.InstructionsAdapter;
import com.example.finalproject.Listeners.InstructionsListener;
import com.example.finalproject.Listeners.RecipeDetailsListener;
import com.example.finalproject.Models.InstructionsResponse;
import com.example.finalproject.Models.RecipeDetailsResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {
    int id;
    TextView textView_dish_name;
    TextView textView_dish_source;
    ImageView imageView_dish_image;
    RecyclerView recycler_dish_ingredients;
    RequestManager manager;
    ProgressDialog dialog;
    IngredientsAdapter ingredientsAdapter;
    RecyclerView recycler_dish_instructions;
    InstructionsAdapter instructionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        findViews();

        id = Integer.parseInt(getIntent().getStringExtra("id"));
        manager = new RequestManager(this);
        manager.getRecipeDetails(recipeDetailsListener, id);
        manager.getInstructions(instructionsListener,id);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Details...");
        dialog.show();
    }

    private void findViews() {
        textView_dish_name = findViewById(R.id.textView_dish_name);
        textView_dish_source = findViewById(R.id.textView_dish_source);
        imageView_dish_image = findViewById(R.id.imageView_dish_image);
        recycler_dish_ingredients = findViewById(R.id.recycler_dish_ingredients);
        recycler_dish_instructions = findViewById(R.id.recycler_dish_instructions);
    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            dialog.dismiss();
            textView_dish_name.setText(response.title);
            textView_dish_source.setText(response.sourceName);
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
    private final InstructionsListener instructionsListener = new InstructionsListener() {
        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            recycler_dish_instructions.setHasFixedSize(true);
            recycler_dish_instructions.setLayoutManager(new LinearLayoutManager(RecipeDetailsActivity.this,LinearLayoutManager.VERTICAL,false));
            instructionsAdapter = new InstructionsAdapter(RecipeDetailsActivity.this,response);
            recycler_dish_instructions.setAdapter(instructionsAdapter);
        }

        @Override
        public void didError(String message) {

        }
    };
}