package com.example.wewantour9;

import android.media.Image;

import com.google.rpc.Help;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;


public class Agency extends User {

    private String agency_name;
    private String telephone_number;
    private String location;
    private String iva_number;

    private ArrayList<Tour> list_tour;
    private ArrayList<Transport> list_transports;

    public Agency() {
        super();
    }

    public Agency(String full_name, String email, String image, int id, String agency_name, String telephone_number,
                  String location, String iva_number) {
        super(full_name, email, image, id);
        this.agency_name = agency_name;
        this.telephone_number = telephone_number;
        this.location = location;
        this.iva_number = iva_number;

        this.list_tour=new ArrayList<Tour>();
        this.list_transports=new ArrayList<Transport>();
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

    /*public void setList_tour(ArrayList<Tour> list_tour) { this.list_tour = list_tour; }*/
    //FUNZIONE CHE CI RISOLVE LA VITAAAAAA
    public void setList_tour(Object list_tour) {
        if(list_tour instanceof ArrayList){
            this.list_tour = (ArrayList<Tour>) list_tour;
        }else if(list_tour instanceof HashMap){
            ArrayList<Tour> buffer = new ArrayList<Tour>();
            HashMap<String, Tour> hashMap_reservation = (HashMap<String, Tour>) list_tour;
            for (Map.Entry<String, Tour> entry : hashMap_reservation.entrySet()) {
                buffer.add(entry.getValue());
            }
            this.list_tour = buffer;
        }
    }

    public ArrayList<Transport> getList_transports() {
        return list_transports;
    }

    /*public void setList_transports(ArrayList<Transport> list_transports) { this.list_transports = list_transports; }*/
    //FUNZIONE CHE CI RISOLVE LA VITAAAAAA
    public void setList_transports(Object list_transports) {
        if(list_transports instanceof ArrayList){
            this.list_transports = (ArrayList<Transport>) list_transports;
        }else if(list_transports instanceof HashMap){
            ArrayList<Transport> buffer = new ArrayList<Transport>();
            HashMap<String, Transport> hashMap_reservation = (HashMap<String, Transport>) list_transports;
            for (Map.Entry<String, Transport> entry : hashMap_reservation.entrySet()) {
                buffer.add(entry.getValue());
            }
            this.list_transports = buffer;
        }
    }

    public void addTour(Tour tour){
        this.list_tour.add(tour);
    }

    public void addTransport(Transport transport){
        this.list_transports.add(transport);
    }

    @Override
    public String toString() {
        return super.toString() + "Agency{" +
                "agency_name='" + agency_name + '\'' +
                ", telephone_number='" + telephone_number + '\'' +
                ", location='" + location + '\'' +
                ", iva_number='" + iva_number + '\'' +
                ", list_tour=" + list_tour +
                ", list_transports=" + list_transports +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Agency agency = (Agency) o;
        return Objects.equals(agency_name, agency.agency_name) &&
                Objects.equals(telephone_number, agency.telephone_number) &&
                Objects.equals(location, agency.location) &&
                Objects.equals(iva_number, agency.iva_number) &&
                Objects.equals(list_tour, agency.list_tour) &&
                Objects.equals(list_transports, agency.list_transports);
    }

    /*public Agency agencyFromJson()*/

}
