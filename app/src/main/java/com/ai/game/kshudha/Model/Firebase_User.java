package com.ai.game.kshudha.Model;
import java.util.ArrayList;

public class Firebase_User {
    private String name, email, password, uid;
    private double lat, lon;


    public Firebase_User(){
    }

    public Firebase_User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void setLocation(double lat, double lon){
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}

