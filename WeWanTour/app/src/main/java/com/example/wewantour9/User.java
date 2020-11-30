package com.example.wewantour9;

import android.media.Image;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User implements Serializable {

    private String full_name;
    private String email;
    private String image;
    private ArrayList<Reservation> list_reservation;


    private int id;


    public User(String full_name, String email,  String image, int id){

        this.full_name = full_name;
        this.email = email;
        this.image = image;
        this.id = id;
        this.list_reservation =  new ArrayList<Reservation>();
    }

    public User(){}

    /*public void setList_reservation(ArrayList<Reservation> list_reservation) { this.list_reservation = list_reservation; }*/
    //FUNZIONE CHE CI RISOLVE LA VITAAAAAA
    public void setList_reservation(Object list_reservation) {
        if(list_reservation instanceof ArrayList){
            this.list_reservation = (ArrayList<Reservation>) list_reservation;
        }else if(list_reservation instanceof HashMap){
            ArrayList<Reservation> buffer = new ArrayList<Reservation>();
            HashMap<String, Reservation> hashMap_reservation = (HashMap<String, Reservation>) list_reservation;
            for (Map.Entry<String, Reservation> entry : hashMap_reservation.entrySet()) {
                buffer.add(entry.getValue());
            }
            this.list_reservation = buffer;
        }
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
                ", image='" + image + '\'' +
                ", id=" + id +
                ", list_reservation=" + list_reservation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(full_name, user.full_name) &&
                Objects.equals(email, user.email) &&
                Objects.equals(image, user.image) &&
                Objects.equals(list_reservation, user.list_reservation);
    }

}

