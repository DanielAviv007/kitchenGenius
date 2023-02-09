package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.Models.IngredientWidget;
import com.example.finalproject.Models.UserRecipe;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class UserRecipeDetailsActivity extends AppCompatActivity {
    public static UserRecipe userRecipe;

    TextView userDetailsDishName;
    ImageView userDetailsDishImage;
    TextView userDetailsDishInstructions;

    TextView[] ingsName;
    TextView[] ingsUnit;
    TextView[] ingsQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_recipe_details);

        userDetailsDishName = findViewById(R.id.userDetailsDishName);
        userDetailsDishImage = findViewById(R.id.userDetailsDishImage);
        userDetailsDishInstructions = findViewById(R.id.userDetailsDishInstructions);

        ingsName = new TextView[]{findViewById(R.id.mainIngredient1NameDetails), findViewById(R.id.mainIngredient2NameDetails), findViewById(R.id.mainIngredient3NameDetails)};
        ingsUnit = new TextView[]{findViewById(R.id.mainIngredient1UnitDetails), findViewById(R.id.mainIngredient2UnitDetails), findViewById(R.id.mainIngredient3UnitDetails)};
        ingsQuantity = new TextView[]{findViewById(R.id.mainIngredient1QuantityDetails), findViewById(R.id.mainIngredient2QuantityDetails), findViewById(R.id.mainIngredient3QuantityDetails)};

        userDetailsDishName.setText(userRecipe.getTitle());

        if (!userRecipe.getPhotoPath().equals("NO_IMAGE")) {
            String photoPath = String.format("gs://recipe-193b1.appspot.com/images/%s", userRecipe.getPhotoPath());
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl(photoPath);

            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(userDetailsDishImage);
                }
            });
        }
        else
            userDetailsDishImage.setImageResource(R.drawable.ic_launcher_background);

        userDetailsDishInstructions.setText(userRecipe.getInstructions());

        for (int i = 0; i < 3; ++i) {
            ingsName[i].setText(userRecipe.getIngredients().get(i).getName());
            ingsUnit[i].setText(userRecipe.getIngredients().get(i).getUnit());
            ingsQuantity[i].setText(String.valueOf(userRecipe.getIngredients().get(i).getQuantity()));
        }
    }
}