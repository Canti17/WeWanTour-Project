package com.example.wewantour9;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

public class Transport implements Serializable {

    private String startLocation;
    private String startDate;
    private String startHour;
    private int minPeople;
    private int currentPeople;
    private int maxPeople;
    private double cost;
    private String vehicle;
    private String destination;
    private String agency;

    public Transport(){}
    public Transport(String startLocation, String startDate, String startHour, int minPeople, int currentPeople, int maxPeople, double cost, String vehicle, String destination, String agency) {
        this.startLocation = startLocation;
        this.startDate = startDate;
        this.startHour = startHour;
        this.minPeople = minPeople;
        this.currentPeople = currentPeople;
        this.maxPeople = maxPeople;
        this.cost = cost;
        this.vehicle = vehicle;
        this.destination = destination;
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

    public int getMinPeople() { return minPeople; }

    public void setMinPeople(int minPeople) { this.minPeople = minPeople; }

    public int getCurrentPeople() {
        return currentPeople;
    }

    public void setCurrentPeople(int currrentPeople) {
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

    public String getDestination() { return destination; }

    public void setDestination(String destination) { this.destination = destination; }

    public String getAgency() { return agency; }

    public void setAgency(String agency) { this.agency = agency; }

    @Override
    public String toString() {
        return "Transport{" +
                "startLocation='" + startLocation + '\'' +
                ", startDate='" + startDate + '\'' +
                ", startHour='" + startHour + '\'' +
                ", minPeople=" + minPeople +
                ", currentPeople=" + currentPeople +
                ", maxPeople=" + maxPeople +
                ", cost=" + cost +
                ", vehicle='" + vehicle + '\'' +
                ", destination='" + destination + '\'' +
                ", agency='" + agency + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transport transport = (Transport) o;
        return minPeople == transport.minPeople &&
                maxPeople == transport.maxPeople &&
                Double.compare(transport.cost, cost) == 0 &&
                Objects.equals(startLocation, transport.startLocation) &&
                Objects.equals(startDate, transport.startDate) &&
                Objects.equals(startHour, transport.startHour) &&
                Objects.equals(vehicle, transport.vehicle) &&
                Objects.equals(destination, transport.destination) &&
                Objects.equals(agency, transport.agency);
    }
}
