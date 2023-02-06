package com.example.finalproject.Adapters;

import android.content.Context;
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
import com.example.finalproject.Models.Recipe;
import com.example.finalproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    Context context;
    List<Recipe> list;
    RecipeClickListener listener;

    public RecipeAdapter(Context context, List<Recipe> list, RecipeClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.recipe_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).title);
        holder.textView_title.setSelected(true);
        holder.textView_likes.setText(list.get(position).aggregateLikes+" Likes");
        holder.textView_servings.setText(list.get(position).servings+ " Servings");
        holder.textView_time.setText(list.get(position).readyInMinutes+" Minutes");
        Picasso.get().load(list.get(position).image).into(holder.imageView_dish);

        holder.list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onRecipeClicked(String.valueOf(list.get(holder.getAdapterPosition()).id));
            }
        });

        holder.imageView_addToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),String.format("%s was added to favourites", holder.textView_title.getText().toString()),Toast.LENGTH_SHORT).show();
                holder.imageView_addToFavourite.setImageResource(R.drawable.ic_added_to_favourites);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class  RecipeViewHolder extends RecyclerView.ViewHolder{
    CardView list_container;
    TextView textView_title;
    TextView textView_servings;
    TextView textView_time;
    TextView textView_likes;
    ImageView imageView_dish;
    ImageView imageView_addToFavourite;

    public RecipeViewHolder(@NonNull View itemView) {
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
