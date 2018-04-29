package com.tst.sleepy.cloudapp.model;

import android.net.Uri;

public class User {

     String name;
    String username;
     String password;
     String photoURL;
     String user_type;

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    User()
     {}




    public User(String name,String username, String password, String user_type,String PhotoURL) {
        this.name=name;
        this.username = username;
        this.password = password;
        this.user_type=user_type;
        this.photoURL=PhotoURL;
    }
}
