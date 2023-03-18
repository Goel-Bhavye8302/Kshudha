package com.ai.game.kshudha.Model;

import java.util.ArrayList;

public class Ngo {
    private String name, reg_no, description;
    private double lat, lon;
    private ArrayList<String> type;

    public Ngo(String name, String reg_no, String description, double lat, double lon) {
        this.name = name;
        this.reg_no = reg_no;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
    }

    public Ngo(String name, String reg_no, String description, double lat, double lon, ArrayList<String> type) {
        this.name = name;
        this.reg_no = reg_no;
        this.description = description;
        this.type = type;
        this.lat = lat;
        this.lon = lon;
    }

    public Ngo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
