package com.example.finalproject.Models;

import android.widget.EditText;
import android.widget.Spinner;

public class IngredientWidget {
    public EditText ingredientName;
    public Spinner unit;
    public EditText quantity;

    public IngredientWidget(EditText ingredientName, Spinner unit, EditText quantity) {
        this.ingredientName = ingredientName;
        this.unit = unit;
        this.quantity = quantity;
    }
}
