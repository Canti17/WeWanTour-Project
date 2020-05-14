package com.example.wewantour9;

import android.media.Image;

import com.google.rpc.Help;

import java.util.ArrayList;
import java.util.LinkedList;


public class Agency extends User {

    private String agency_name;
    private String telephone_number;
    private String location;
    private String iva_number;

    private ArrayList<Tour> list_tour;
    private LinkedList<Transport> list_transports;

    public Agency() {
        super();
    }

    public Agency(String full_name, String email, String password, Image image, int id, String agency_name, String telephone_number,
                  String location, String iva_number) {
        super(full_name, email, password, image, id);
        this.agency_name = agency_name;
        this.telephone_number = telephone_number;
        this.location = location;
        this.iva_number = iva_number;

        this.list_tour=new ArrayList<>();
        this.list_transports=new LinkedList<Transport>();
    }

    public Agency(){
        super();
    }


    public String getAgency_name() {
        return agency_name;
    }

    public String getTelephone_number() {
        return telephone_number;
    }

    public void setTelephone_number(String telephone_number) {
        this.telephone_number = telephone_number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIva_number() {
        return iva_number;
    }

    public void setIva_number(String iva_number) {
        this.iva_number = iva_number;
    }

    public ArrayList<Tour> getList_tour() {
        return list_tour;
    }

    public void setList_tour(ArrayList<Tour> list_tour) {
        this.list_tour = list_tour;
    }

    public LinkedList<Transport> getList_transports() {
        return list_transports;
    }

    public void setList_transports(LinkedList<Transport> list_transports) {
        this.list_transports = list_transports;
    }

    public void addTour(Tour tour){
        this.list_tour.add(tour);
    }

    public void addTransport(Transport transport){
        this.list_transports.add(transport);
    }

}
