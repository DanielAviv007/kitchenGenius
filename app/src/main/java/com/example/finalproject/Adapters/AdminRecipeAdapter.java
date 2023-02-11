package com.example.finalproject.Adapters;

import android.content.Intent;
import android.net.Uri;
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

import com.example.finalproject.FeedActivity;
import com.example.finalproject.Models.UserRecipe;
import com.example.finalproject.R;
import com.example.finalproject.Storage.SystemStorage;
import com.example.finalproject.UserRecipeDetailsActivity;
import com.example.finalproject.addRecipeActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminRecipeAdapter extends RecyclerView.Adapter<AdminRecipeAdapter.MyViewHolder>{
    private ArrayList<UserRecipe> dataSet;

    public AdminRecipeAdapter(ArrayList<UserRecipe> dataSet) {
        this.dataSet = dataSet;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView adminDishTitle;
        ImageView editBtn;
        ImageView deleteBtn;
        ImageView adminDishImage;
        TextView adminDishServings;
        TextView adminDishTime;

        public MyViewHolder (View itemView) {
            super(itemView);

            adminDishTitle = itemView.findViewById(R.id.adminDishTitle);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            adminDishImage = itemView.findViewById(R.id.adminDishImage);
            adminDishServings = itemView.findViewById(R.id.adminDishServings);
            adminDishTime = itemView.findViewById(R.id.adminDishTime);
        }
    }

    @NonNull
    @Override
    public AdminRecipeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_list, parent, false);

        AdminRecipeAdapter.MyViewHolder myViewHolder = new AdminRecipeAdapter.MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminRecipeAdapter.MyViewHolder viewHolder, int listPosition) {
        TextView adminDishTitle = viewHolder.adminDishTitle;
        ImageView editBtn = viewHolder.editBtn;
        ImageView deleteBtn = viewHolder.deleteBtn;
        ImageView adminDishImage = viewHolder.adminDishImage;
        TextView adminDishServings = viewHolder.adminDishServings;
        TextView adminDishTime = viewHolder.adminDishTime;;

        if (!dataSet.get(listPosition).getPhotoPath().equals("NO_IMAGE")) {
            String photoPath = String.format("gs://recipe-193b1.appspot.com/images/%s", dataSet.get(listPosition).getPhotoPath());
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(photoPath);

            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(adminDishImage);
                }
            });
        }
        else
            adminDishImage.setImageResource(R.drawable.default_image);

        adminDishTitle.setText(dataSet.get(listPosition).getTitle());
        adminDishServings.setText(String.valueOf(dataSet.get(listPosition).getServings()));
        adminDishTime.setText(String.valueOf(dataSet.get(listPosition).getTimeInMinutes()));

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecipeFromDataBase(dataSet.get(listPosition), FirebaseDatabase.getInstance());
                addRecipeActivity.userRecipe = dataSet.get(listPosition);

                Intent intent = new Intent();
                intent.setClass(v.getContext(), addRecipeActivity.class);
                v.getContext().startActivity(intent);

                notifyDataSetChanged();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = dataSet.get(listPosition).getTitle();
                deleteRecipeFromDataBase(dataSet.get(listPosition), FirebaseDatabase.getInstance());
                dataSet.clear();
                notifyDataSetChanged();

                Toast.makeText(v.getContext(),String.format("%s was deleted successfully", title),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void deleteRecipeFromDataBase(UserRecipe userRecipe, FirebaseDatabase database) {
        DatabaseReference ref = database.getReference("recipes");

        String uid = userRecipe.getUid();
        String title = userRecipe.getTitle();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    if (snapshot.child("uid").getValue().equals(uid) && snapshot.child("title").getValue().equals(title)) {
                        snapshot.getRef().removeValue();
                        break;
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { Log.e("Firebase", "onCancelled", databaseError.toException()); }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
