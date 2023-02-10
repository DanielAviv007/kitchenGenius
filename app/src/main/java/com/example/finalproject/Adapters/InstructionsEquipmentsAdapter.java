package com.example.finalproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Models.Equipment;
import com.example.finalproject.Models.Ingredient;
import com.example.finalproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InstructionsEquipmentsAdapter extends RecyclerView.Adapter<InstructionEquipmentsViewHolder> {
    Context context;
    List<Equipment> equipments_list;


    public InstructionsEquipmentsAdapter(Context context, List<Equipment> equipments_list) {
        this.context = context;
        this.equipments_list = equipments_list;
    }

    @NonNull
    @Override
    public InstructionEquipmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionEquipmentsViewHolder(LayoutInflater.from(context).inflate(R.layout.instructions_step_items_list,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull InstructionEquipmentsViewHolder holder, int position) {
        holder.textView_instructions_step_item.setText(equipments_list.get(position).name);
        holder.textView_instructions_step_item.setSelected(true);
        Picasso.get().load("https://spoonacular.com/cdn/equipment_100x100/" + equipments_list.get(position).image).into(holder.imageView_instructions_step_item);

    }

    @Override
    public int getItemCount() {
        return equipments_list.size();
    }
}

class InstructionEquipmentsViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView_instructions_step_item;
    TextView textView_instructions_step_item;

    public  InstructionEquipmentsViewHolder(@NonNull View itemView){
        super(itemView);
        imageView_instructions_step_item = itemView.findViewById(R.id.imageView_instructions_step_item);
        textView_instructions_step_item = itemView.findViewById(R.id.textView_instructions_step_item);
    }
}
