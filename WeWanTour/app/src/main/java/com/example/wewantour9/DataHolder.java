package com.example.wewantour9;

public class DataHolder {
    private int data = 0;  //PEDOMETER CHOICE
    private String agencycustomer = "customer"; //IM in AGENCY or CUSTOMER
    public int getData() {return data;}
    public void setData(int data) {this.data = data;}

    public String getAgencycustomer() {return agencycustomer;}
    public void setAgencycustomer(String agencycustomer) {this.agencycustomer = agencycustomer;}

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
}
