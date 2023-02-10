package com.example.finalproject.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Models.Ingredient;
import com.example.finalproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstructionsIngredientsAdapter extends RecyclerView.Adapter<InstructionIngredientsViewHolder> {
    Context context;
    List<Ingredient> ingredients_list;


    public InstructionsIngredientsAdapter(Context context, List<Ingredient> ingredients_list) {
        this.context = context;
        this.ingredients_list = ingredients_list;
    }

    @NonNull
    @Override
    public InstructionIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionIngredientsViewHolder(LayoutInflater.from(context).inflate(R.layout.instructions_step_items_list,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull InstructionIngredientsViewHolder holder, int position) {
        holder.textView_instructions_step_item.setText(ingredients_list.get(position).name);
        holder.textView_instructions_step_item.setSelected(true);
        Picasso.get().load("https://spoonacular.com/cdn/ingredients_100x100/" + ingredients_list.get(position).image).into(holder.imageView_instructions_step_item);
    }

    @Override
    public int getItemCount() {
        return ingredients_list.size();
    }
}

class InstructionIngredientsViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView_instructions_step_item;
    TextView textView_instructions_step_item;

    public  InstructionIngredientsViewHolder(@NonNull View itemView){
        super(itemView);
        imageView_instructions_step_item = itemView.findViewById(R.id.imageView_instructions_step_item);
        textView_instructions_step_item = itemView.findViewById(R.id.textView_instructions_step_item);
    }
}
