package com.example.finalproject.Storage;

import android.util.Log;
import android.widget.Toast;

import com.example.finalproject.FeedActivity;
import com.example.finalproject.Models.Us;
import com.example.finalproject.Models.User;
import com.example.finalproject.Models.UserIngredient;
import com.example.finalproject.Models.UserRecipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class SystemStorage {
    private static String uid;

    public static void setCurrentUID(String uid) { SystemStorage.uid = uid; }
    public static String getCurrentUID() { return SystemStorage.uid; }
}
