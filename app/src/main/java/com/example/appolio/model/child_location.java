package com.example.appolio.model;

public class child_location {
    String latitude,longitude,childname,address,vaccinename;

    public child_location() {
    }

    public child_location(String latitude, String longitude, String childname, String address, String vaccinename) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.childname = childname;
        this.address = address;
        this.vaccinename = vaccinename;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getChildname() {
        return childname;
    }

    public String getAddress() {
        return address;
    }

    public String getVaccinename() {
        return vaccinename;
    }
}
