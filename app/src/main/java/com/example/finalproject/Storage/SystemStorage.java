package com.example.finalproject.Storage;

import com.example.finalproject.Models.Us;
import com.example.finalproject.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SystemStorage {
    private static String uid;

    public static void setCurrentUID(String uid) { SystemStorage.uid = uid; }
    public static String getCurrentUID() { return SystemStorage.uid; }
}
