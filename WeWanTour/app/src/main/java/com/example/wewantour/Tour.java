package com.example.wewantour;

public class Tour {
    private String name;
    private String description;
    private String clients;


    public Tour(String name, String description, String clients) {
        this.name = name;
        this.description = description;
        this.clients = clients;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getClients() {
        return clients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setClients(String clients) {
        this.clients = clients;
    }
}
