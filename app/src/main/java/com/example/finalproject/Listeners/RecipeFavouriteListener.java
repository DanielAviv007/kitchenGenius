package com.example.finalproject.Listeners;

import com.example.finalproject.Models.RandomRecipeApiResponse;
import com.example.finalproject.Models.RecipeFavouriteResponse;

public interface RecipeFavouriteListener {
    void didFetch(RecipeFavouriteResponse response, String message);
    void didError(String message);
}
