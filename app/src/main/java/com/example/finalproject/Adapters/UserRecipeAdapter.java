package com.example.finalproject.Adapters;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Models.UserRecipe;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
public class UserRecipeAdapter extends RecyclerView.Adapter<UserRecipeAdapter.MyViewHolder>  {

    private ArrayList<UserRecipe> dataSet;

    public UserRecipeAdapter(ArrayList<UserRecipe> dataSet) {
        this.dataSet = dataSet;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        CardView userRecipeCard;
        TextView userDishTitle;
        ImageView userDishImage;
        TextView userDishServings;
        TextView userDishTime;


        public MyViewHolder (View itemView) {
            super(itemView);

            userRecipeCard = itemView.findViewById(R.id.userRecipeCard);
            userDishTitle = itemView.findViewById(R.id.userDishTitle);
            userDishImage = itemView.findViewById(R.id.userDishImage);
            userDishServings = itemView.findViewById(R.id.userDishServings);
            userDishTime = itemView.findViewById(R.id.userDishTime);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext() ).inflate(R.layout.user_recipes_card, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int listPosition) {

        CardView userRecipeCard = viewHolder.userRecipeCard;
        TextView userDishTitle = viewHolder.userDishTitle;
        ImageView userDishImage = viewHolder.userDishImage;
        TextView userDishServings = viewHolder.userDishServings;
        TextView userDishTime = viewHolder.userDishTime;

        if (!dataSet.get(listPosition).getPhotoPath().equals("NO_IMAGE")) {
            String photoPath = String.format("gs://recipe-193b1.appspot.com/images/%s", dataSet.get(listPosition).getPhotoPath());
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(photoPath);

            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(userDishImage);
                }
            });
        }
        else
            userDishImage.setImageResource(R.drawable.ic_launcher_background);
        userDishTitle.setText(dataSet.get(listPosition).getTitle());
        userDishServings.setText(String.valueOf(dataSet.get(listPosition).getServings()));
        userDishTime.setText(String.valueOf(dataSet.get(listPosition).getTimeInMinutes()));

// TODO:
//        userRecipeCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                int num = listPosition;
//                String position = String.valueOf(num);
//                bundle.putString("characterId",position);
//                Navigation.findNavController(view).navigate(R.id.action_blankFragmentHome_to_blankFragmentDescription,bundle);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
