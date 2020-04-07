package com.example.wewantour9;

import java.util.Calendar;

public class Transport {

    private String startLocation;
    private Calendar startDate;
    private int CurrrentPeople;
    private int maxPeople;
    private double cost;

    public Transport(String startLocation, Calendar startDate, int currrentPeople, int maxPeople, double cost) {
        this.startLocation = startLocation;
        this.startDate = startDate;
        CurrrentPeople = currrentPeople;
        this.maxPeople = maxPeople;
        this.cost = cost;
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
        return CurrrentPeople;
    }

    public void setCurrrentPeople(int currrentPeople) {
        CurrrentPeople = currrentPeople;
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

    @Override
    public String toString() {
        return "Transport{" +
                "startLocation='" + startLocation + '\'' +
                ", startDate=" + startDate.get(Calendar.DAY_OF_MONTH) + "/" +
                                    (startDate.get(Calendar.MONTH)+1) + "/" +
                                       startDate.get(Calendar.YEAR) + " H:" +
                                  startDate.get(Calendar.HOUR_OF_DAY) + ":" +
                                             startDate.get(Calendar.MINUTE) +
                ", CurrrentPeople=" + CurrrentPeople +
                ", maxPeople=" + maxPeople +
                ", cost=" + cost +
                '}';
    }
}
