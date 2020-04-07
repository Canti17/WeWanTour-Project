package com.example.wewantour9;

import android.media.Image;

public class User {

    private final String full_name;
    private String email;
    private String password;
    private Image image;


    private final int id;

    public User(String full_name, String email, String password, Image image, int id){

        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.image = image;
        this.id = id;

    }





    public String getFull_name() {
        return full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }



}

