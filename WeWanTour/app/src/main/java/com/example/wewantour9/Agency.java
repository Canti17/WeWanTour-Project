package com.example.wewantour9;

import android.media.Image;



public class Agency extends User {

    private final String agency_name;
    private String telephone_number;
    private String location;
    private String iva_number;

    public Agency(String full_name, String email, String password, Image image, int id, String agency_name, String telephone_number,
                  String location, String iva_number) {
        super(full_name, email, password, image, id);
        this.agency_name = agency_name;
        this.telephone_number = telephone_number;
        this.location = location;
        this.iva_number = iva_number;

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

}
