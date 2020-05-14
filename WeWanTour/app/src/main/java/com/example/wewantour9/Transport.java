package com.example.wewantour9;

import java.util.Calendar;

public class Transport {

    private String startLocation;
    private String startDate;
    private String startHour;
    private int currentPeople;
    private int maxPeople;
    private double cost;
    private String vehicle;
    private Tour tour;
    private Agency agency;

    public Transport(){}
    public Transport(String startLocation, String startDate, String startHour, int currrentPeople, int maxPeople, double cost, String vehicle, Tour tour, Agency agency) {
        this.startLocation = startLocation;
        this.startDate = startDate;
        this.startHour = startHour;
        this.currentPeople = currrentPeople;
        this.maxPeople = maxPeople;
        this.cost = cost;
        this.vehicle = vehicle;
        this.tour = tour;
        this.agency = agency;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartHour() { return startHour; }

    public void setStartHour(String startHour) { this.startHour = startHour; }

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

    public Tour getTour() { return tour; }

    public void setTour(Tour tour) { this.tour = tour; }

    public Agency getAgency() { return agency; }

    public void setAgency(Agency agency) { this.agency = agency; }

    @Override
    public String toString() {
        return "Transport{" +
                "startLocation='" + startLocation + '\'' +
                ", startDate='" + startDate + '\'' +
                ", startHour='" + startHour + '\'' +
                ", currentPeople=" + currentPeople +
                ", maxPeople=" + maxPeople +
                ", cost=" + cost +
                ", vehicle='" + vehicle + '\'' +
                ", tour=" + tour +
                ", agency=" + agency +
                '}';
    }
}
