package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.finalproject.Models.IngredientWidget;
import com.example.finalproject.Models.UserIngredient;
import com.example.finalproject.Models.UserRecipe;
import com.example.finalproject.Storage.SystemStorage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class addRecipeActivity extends AppCompatActivity {
    public static UserRecipe userRecipe = null;
    Uri imageUri;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    ImageView recipeImage;
    Button selectImageBtn;
    Button submitBtn;
    EditText recipeTitle;
    EditText prepTime;
    Spinner servings;
    EditText instruction;
    IngredientWidget[] ingredients = new IngredientWidget[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        recipeTitle = findViewById(R.id.recipeTitle);

        ingredients[0] = new IngredientWidget(findViewById(R.id.mainIngredient1Name), findViewById(R.id.unitSpinner1), findViewById(R.id.quantity1));
        ingredients[1] = new IngredientWidget(findViewById(R.id.mainIngredient2Name), findViewById(R.id.unitSpinner2), findViewById(R.id.quantity2));
        ingredients[2] = new IngredientWidget(findViewById(R.id.mainIngredient3Name), findViewById(R.id.unitSpinner3), findViewById(R.id.quantity3));

        prepTime = findViewById(R.id.prepTime);
        servings = findViewById(R.id.servingsSpinner);
        instruction = findViewById(R.id.instructions);
        recipeImage = findViewById(R.id.imageView_new_recipe_image);
        selectImageBtn = findViewById(R.id.selectImageBtn);
        submitBtn = findViewById(R.id.submitBtn);

        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { selectImage(); }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { uploadRecipe(); }
        });

        if (userRecipe != null) {
            recipeTitle.setText(userRecipe.getTitle());

            for (int i = 0; i < 3; ++i) {
                ingredients[i].ingredientName.setText(userRecipe.getIngredients().get(i).getName());
                ingredients[i].quantity.setText(String.valueOf(userRecipe.getIngredients().get(i).getQuantity()));
            }
            prepTime.setText(String.valueOf(userRecipe.getTimeInMinutes()));
            instruction.setText(userRecipe.getInstructions());

            if (!userRecipe.getPhotoPath().equals("NO_IMAGE")) {
                String photoPath = String.format("gs://recipe-193b1.appspot.com/images/%s", userRecipe.getPhotoPath());
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl(photoPath);

                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(recipeImage);
                    }
                });
            }
            else
                recipeImage.setImageResource(R.drawable.default_image);
        }
    }
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            recipeImage.setImageURI(imageUri);
        }
    }

    private void uploadRecipe() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);

        if (imageUri != null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading File...");
            progressDialog.show();


            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            recipeImage.setImageURI(null);
                            Toast.makeText(addRecipeActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Toast.makeText(addRecipeActivity.this, "Failed to Upload", Toast.LENGTH_SHORT).show();

                        }
                    });
        }

        String s_recipeTitle = recipeTitle.getText().toString();
        String s_instruction = instruction.getText().toString();
        String[] ingredients_names = {ingredients[0].ingredientName.getText().toString(), ingredients[1].ingredientName.getText().toString(), ingredients[2].ingredientName.getText().toString()};
        String[] ingredients_units = {ingredients[0].unit.getSelectedItem().toString(), ingredients[1].unit.getSelectedItem().toString(), ingredients[2].unit.getSelectedItem().toString()};
        Double[] ingredients_quantities = {Double.parseDouble(ingredients[0].quantity.getText().toString()), Double.parseDouble(ingredients[1].quantity.getText().toString()), Double.parseDouble(ingredients[2].quantity.getText().toString())};
        ArrayList<UserIngredient> ings = new ArrayList<>();
        Integer i_prepTime = Integer.parseInt(prepTime.getText().toString());
        Integer i_servings = Integer.parseInt(servings.getSelectedItem().toString());

        ings.add(new UserIngredient(ingredients_names[0], ingredients_units[0], ingredients_quantities[0]));
        ings.add(new UserIngredient(ingredients_names[1], ingredients_units[1], ingredients_quantities[1]));
        ings.add(new UserIngredient(ingredients_names[2], ingredients_units[2], ingredients_quantities[2]));

        if (s_recipeTitle == null || s_recipeTitle.isEmpty())
            Toast.makeText(addRecipeActivity.this, "You must provide a title!", Toast.LENGTH_SHORT).show();

        else if (imageUri == null && (s_instruction == null || s_instruction.isEmpty()))
            Toast.makeText(addRecipeActivity.this, "In order to create a recipe, you must upload an image or fill the instructions", Toast.LENGTH_SHORT).show();
        else if (SystemStorage.getCurrentUID().equals("vRtxwdfLhMRBffxhJxJgYo0G5sK2"))
            new UserRecipe(userRecipe.getUid(), s_recipeTitle, imageUri != null ? fileName : "NO_IMAGE", ings, i_servings, i_prepTime, s_instruction);
        else
            new UserRecipe(SystemStorage.getCurrentUID(), s_recipeTitle, imageUri != null ? fileName : "NO_IMAGE", ings, i_servings, i_prepTime, s_instruction);

        if (SystemStorage.getCurrentUID().equals("vRtxwdfLhMRBffxhJxJgYo0G5sK2")) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent();
            intent.setClass(addRecipeActivity.this, AdminActivity.class);
            addRecipeActivity.this.startActivity(intent);
        } else {
            Intent intent = new Intent();
            intent.setClass(addRecipeActivity.this, FeedActivity.class);
            addRecipeActivity.this.startActivity(intent);
        }
    }

}