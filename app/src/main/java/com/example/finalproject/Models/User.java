package com.example.finalproject.Models;

import android.widget.Toast;

import com.example.finalproject.Storage.SystemStorage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class User {
    private String email;
    private String nickname;
    private String uid;

    public User(String email, String uid) {
        this.email = email;
        this.nickname = new String(email.substring(0, email.indexOf('@')));
        this.uid = uid;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getNickname() { return nickname; }

    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }
}
