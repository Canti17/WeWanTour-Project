package com.example.wewantour9;

import java.util.Calendar;

public class Transport {

    private String startLocation;
    private Calendar startDate;
    private int currentPeople;
    private int maxPeople;
    private double cost;
    private String vehicle;

    public Transport(String startLocation, Calendar startDate, int currrentPeople, int maxPeople, double cost, String vehicle) {
        this.startLocation = startLocation;
        this.startDate = startDate;
        this.currentPeople = currrentPeople;
        this.maxPeople = maxPeople;
        this.cost = cost;
        this.vehicle = vehicle;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public int getCurrrentPeople() {
        return currentPeople;
    }

    public void setCurrrentPeople(int currrentPeople) {
        currentPeople = currrentPeople;
    }

    public int getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(int maxPeople) {
        this.maxPeople = maxPeople;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getVehicle() { return vehicle; }

    public void setVehicle(String vehicle) { this.vehicle = vehicle; }

    @Override
    public String toString() {
        return "Transport{" +
                "startLocation='" + startLocation + '\'' +
                ", startDate=" + startDate.get(Calendar.DAY_OF_MONTH) + "/" +
                                    (startDate.get(Calendar.MONTH)+1) + "/" +
                                       startDate.get(Calendar.YEAR) + " H:" +
                                  startDate.get(Calendar.HOUR_OF_DAY) + ":" +
                                             startDate.get(Calendar.MINUTE) +
                ", CurrrentPeople=" + currentPeople +
                ", maxPeople=" + maxPeople +
                ", cost=" + cost +
                ", vehicle" + vehicle +
                '}';
    }
}
