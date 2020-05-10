package com.example.wewantour9;

import android.widget.TableRow;

import java.util.Calendar;
import java.util.LinkedList;

public class Tour {
    private String name;
    private String description;
    private String startPlace;
    private Calendar calendar;
    private double price;
    private double duration;
    private int currentPeople;
    private int peopleLimit;
    private String vehicle;
    private Agency agency;
    private LinkedList<Transport> transports;



    public Tour(String name, String description, String startPlace, Calendar calendar, double price, double duration, int currentPeople, int peopleLimit, String vehicle, Agency agency) {
        this.name = name;
        this.description = description;
        this.startPlace = startPlace;
        this.calendar = calendar;
        this.price = price;
        this.duration = duration;
        this.currentPeople = currentPeople;
        this.peopleLimit = peopleLimit;
        this.vehicle = vehicle;
        this.agency=agency;
        this.transports= new LinkedList<Transport>();
    }

    public LinkedList<Transport> getTransports() { return transports; }

    public void setTransports(LinkedList<Transport> transports) { this.transports = transports; }

    public void addTransport(Transport transport){ this.transports.add(transport);}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getCurrentPeople() {
        return currentPeople;
    }

    public void setCurrentPeople(int currentPeople) {
        this.currentPeople = currentPeople;
    }

    public int getPeopleLimit() {
        return peopleLimit;
    }

    public void setPeopleLimit(int peopleLimit) {
        this.peopleLimit = peopleLimit;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public Agency getAgency() { return agency; }

    public void setAgency(Agency agency) { this.agency = agency; }
}
