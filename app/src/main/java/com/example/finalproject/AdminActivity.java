package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.finalproject.Adapters.AdminRecipeAdapter;
import com.example.finalproject.Adapters.UserRecipeAdapter;
import com.example.finalproject.Models.UserIngredient;
import com.example.finalproject.Models.UserRecipe;
import com.example.finalproject.Storage.SystemStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private ArrayList<UserRecipe> dataSet;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private AdminRecipeAdapter adapter;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        recyclerView = findViewById(R.id.recycler_admin_recipes);
        layoutManager = new LinearLayoutManager(this);
        dataSet = new ArrayList<>();
        database = FirebaseDatabase.getInstance().getReference("recipes");

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new AdminRecipeAdapter(dataSet);
        recyclerView.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                    dataSet.add(new UserRecipe(uid, title, photoPath, ingredients, servings, timeInMinutes, instructions));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    public void deleteRecipe() {

    }
}