package com.example.wewantour9;

public class Reservation {
    private Tour tour;
    private Transport transport;
    private Customer customer;

    public Reservation(Tour tour, Transport transport, Customer customer) {
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
