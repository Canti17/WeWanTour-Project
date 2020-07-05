package com.example.wewantour9;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String full_name;
    private String email;
    private String password;
    private String image;
    private ArrayList<Reservation> list_reservation;


    private int id;


    public User(String full_name, String email, String password, String image, int id){

        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.image = image;
        this.id = id;
        this.list_reservation =  new ArrayList<Reservation>();
    }

    public User(){}

    public void setList_reservation(ArrayList<Reservation> list_reservation) {
        this.list_reservation = list_reservation;
    }

    public ArrayList<Reservation> getList_reservation() {
        return list_reservation;
    }

    public void addReservation(Reservation reserve){
        this.list_reservation.add(reserve);
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "full_name='" + full_name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                ", id=" + id +
                '}';
    }
}

