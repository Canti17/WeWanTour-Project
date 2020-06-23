package com.example.wewantour9;

import java.io.Serializable;

public class Reservation implements Serializable {
    private Tour tour;
    private Transport transport;
    private String customer;
    private int numberOfPeople;

    public Reservation(){}
    public Reservation(Tour tour, Transport transport, String customer, int numberOfPeople) {
        this.tour = tour;
        this.transport = transport;
        this.customer = customer;
        this.numberOfPeople = numberOfPeople;
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

    @Override
    public String toString() {
        return "Reservation{" + "\n" +
                "tour=" + tour + "\n\n\n" +
                "transport=" + transport + "\n\n\n" +
                "customer='" + customer + "\n\n\n" +
                "numberOfPeople=" + numberOfPeople + "\n" +
                '}';
    }
}
