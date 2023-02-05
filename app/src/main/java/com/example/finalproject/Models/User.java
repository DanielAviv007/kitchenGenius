package com.example.finalproject.Models;

public class User {
    private String email;
    private String nickname;
    private String uid;

    public User(String email, String uid) {
        this.email = email;
        this.nickname = new String(email.substring(0, email.indexOf('@')));
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }
}
