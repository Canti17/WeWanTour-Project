package com.example.wewantour9;

import android.media.Image;

import java.util.ArrayList;
import java.util.LinkedList;

public class Customer extends User {

    private ArrayList<Reservation> list_reservation;

    public Customer(String full_name, String email, String password, Image image, int id) {
        super(full_name, email, password, image, id);

        this.list_reservation =  new ArrayList<Reservation>();
    }

    public Customer(){
        super();
    }

    public void setList_reservation(ArrayList<Reservation> list_reservation) {
        this.list_reservation = list_reservation;
    }

    public ArrayList<Reservation> getList_reservation() {
        return list_reservation;
    }

    public void addReservation(Reservation reserve){
        this.list_reservation.add(reserve);
    }



}
