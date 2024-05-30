package com.example.hoangvancook.Models;

import android.widget.ImageView;

public class User {
    String uid;
    String name;
    String email;
    String photoUrl;

    public User() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public User(String uid, String name, String email, String photoUrl) {
        this.uid  = uid;
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
    }
}
