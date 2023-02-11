package com.example.finalproject.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Listeners.RecipeClickListener;
import com.example.finalproject.Models.FavouriteRecipe;
import com.example.finalproject.Models.Recipe;
import com.example.finalproject.R;
import com.example.finalproject.Storage.SystemStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavouriteRecipeAdapter extends RecyclerView.Adapter<FavouriteRecipeViewHolder> {
    Context context;
    ArrayList<Recipe> dataSet;
    RecipeClickListener listener;

    public FavouriteRecipeAdapter(Context context, ArrayList<Recipe> dataSet, RecipeClickListener listener) {
        this.context = context;
        this.dataSet = dataSet;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavouriteRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavouriteRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteRecipeViewHolder holder, int position) {
        holder.textView_title.setText(dataSet.get(position).title);
        holder.textView_title.setSelected(true);
        holder.textView_likes.setText(dataSet.get(position).aggregateLikes+" Likes");
        holder.textView_servings.setText(dataSet.get(position).servings+ " Servings");
        holder.textView_time.setText(dataSet.get(position).readyInMinutes+" Minutes");
        Picasso.get().load(dataSet.get(position).image).into(holder.imageView_dish);
        holder.imageView_addToFavourite.setImageResource(R.drawable.ic_added_to_favourites);

        holder.list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecipeClicked(String.valueOf(dataSet.get(holder.getAdapterPosition()).id));
            }
        });

        holder.imageView_addToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView favouriteImage = holder.imageView_addToFavourite;
                Drawable currentImage = favouriteImage.getDrawable();
                Drawable checkedImage = holder.checked;

                Toast.makeText(v.getContext(),String.format("%s was removed from favourites", holder.textView_title.getText().toString()),Toast.LENGTH_SHORT).show();
                removeElementFromFavouriteRecipes(SystemStorage.getCurrentUID(), String.valueOf(dataSet.get(holder.getAdapterPosition()).id), FirebaseDatabase.getInstance());
                dataSet.clear();
                notifyDataSetChanged();
            }
            public void removeElementFromFavouriteRecipes(String uid, String recipeId, FirebaseDatabase database) {
                DatabaseReference ref = database.getReference("favourite_recipes");

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.child("uid").getValue().equals(uid) && String.valueOf(snapshot.child("recipe").child("id").getValue()).equals(recipeId)) {
                                snapshot.getRef().removeValue();
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "onCancelled", databaseError.toException());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

class  FavouriteRecipeViewHolder extends RecyclerView.ViewHolder{
    CardView list_container;
    TextView textView_title;
    TextView textView_servings;
    TextView textView_time;
    TextView textView_likes;
    ImageView imageView_dish;
    ImageView imageView_addToFavourite;
    Drawable checked;

    public FavouriteRecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        list_container = itemView.findViewById(R.id.list_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_servings = itemView.findViewById(R.id.textView_servings);
        textView_time = itemView.findViewById(R.id.textView_time);
        textView_likes = itemView.findViewById(R.id.textView_likes);
        imageView_dish = itemView.findViewById(R.id.imageView_dish);
        imageView_addToFavourite = itemView.findViewById(R.id.imageView_fav);
    }
}
