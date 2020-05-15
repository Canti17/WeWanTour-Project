package com.example.wewantour9;

public class Reservation {
    private Tour tour;
    private Transport transport;
    private String customer;

    public Reservation(Tour tour, Transport transport, String customer) {
        this.tour = tour;
        this.transport = transport;
        this.customer = customer;
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
}
