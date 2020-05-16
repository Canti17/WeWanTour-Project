package com.example.wewantour9;

import android.widget.TableRow;

import java.util.Calendar;
import java.util.LinkedList;

public class Tour {
    private String name;
    private String description;
    private String startPlace;
    private String startDate;
    private String startHour;
    private double price;
    private double duration;
    private int currentPeople;
    private int minPeople;
    private int peopleLimit;
    private String vehicle;
    private String agency;
    private LinkedList<Transport> transports;
    private String filePath;



    public Tour(){
    }


    public Tour(String name, String description, String startPlace, String startDate, String startHour, double price, double duration, int currentPeople, int peopleLimit,int minPeople, String vehicle, String agency, String filePath) {
        this.name = name;
        this.description = description;
        this.startPlace = startPlace;
        this.startDate= startDate;
        this.startHour= startHour;
        this.price = price;
        this.duration = duration;
        this.currentPeople = currentPeople;
        this.peopleLimit = peopleLimit;
        this.minPeople=minPeople;
        this.vehicle = vehicle;
        this.agency=agency;
        this.transports= new LinkedList<Transport>();
        this.filePath= filePath;
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

    public String getStartDate() { return startDate; }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getStartHour() { return startHour; }

    public void setStartHour(String startHour) { this.startHour = startHour; }

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

    public int getMinPeople() { return minPeople; }

    public void setMinPeople(int minPeople) { this.minPeople = minPeople; }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getAgency() { return agency; }

    public void setAgency(String agency) { this.agency = agency; }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
