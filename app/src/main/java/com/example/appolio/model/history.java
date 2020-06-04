package com.example.appolio.model;

public class history {

    String childname ,fathername,mothername,address,vaccine_name,cninc;

    public history() {
    }

    public history(String childname, String fathername, String mothername, String address, String vaccine_name, String cninc) {
        this.childname = childname;
        this.fathername = fathername;
        this.mothername = mothername;
        this.address = address;
        this.vaccine_name = vaccine_name;
        this.cninc = cninc;
    }

    public String getChildname() {
        return childname;
    }

    public String getCninc() {
        return cninc;
    }

    public String getFathername() {
        return fathername;
    }

    public String getMothername() {
        return mothername;
    }

    public String getAddress() {
        return address;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }
}
