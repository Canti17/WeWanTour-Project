package com.example.wewantour9;

import java.io.Serializable;

public class Reservation implements Serializable {
    private Tour tour;
    private Transport transport;
    private String customer;
    private int numberOfPeople;
    private int transportNumberOfPeople;

    public Reservation(){}
    public Reservation(Tour tour, Transport transport, String customer, int numberOfPeople, int transportNumberOfPeople) {
        this.tour = tour;
        this.transport = transport;
        this.customer = customer;
        this.numberOfPeople = numberOfPeople;
        this.transportNumberOfPeople = transportNumberOfPeople;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getNumberOfPeople() { return numberOfPeople; }

    public void setNumberOfPeople(int numberOfPeople) { this.numberOfPeople = numberOfPeople; }

    public int getTransportNumberOfPeople() { return transportNumberOfPeople; }

    public void setTransportNumberOfPeople(int transportNumberOfPeople) { this.transportNumberOfPeople = transportNumberOfPeople; }

    @Override
    public String toString() {
        return "Reservation{" +
                "tour=" + tour +
                ", transport=" + transport +
                ", customer='" + customer + '\'' +
                ", numberOfPeople=" + numberOfPeople +
                ", transportNumberOfPeople=" + transportNumberOfPeople +
                '}';
    }
}
