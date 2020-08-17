package com.example.appolio.model;

public class model_child {
    String fathername ,mothername ,childname ,phonenumber ,address,childgender,dateofBirth,bForm,cninc,emial;
    double latitude,longitude;
    String company_name ,vaccine_name,vaccine_quality,vaccine_type,vaccine_id,workerCnic,vaccination_date;


    public model_child() {
    }

    public model_child(String fathername, String mothername, String childname, String phonenumber, String address, String childgender, String dateofBirth, String bForm, String cninc, String emial, double latitude, double longitude, String company_name, String vaccine_name, String vaccine_quality, String vaccine_type, String vaccine_id, String workerCnic, String vaccination_date) {
        this.fathername = fathername;
        this.mothername = mothername;
        this.childname = childname;
        this.phonenumber = phonenumber;
        this.address = address;
        this.childgender = childgender;
        this.dateofBirth = dateofBirth;
        this.bForm = bForm;
        this.cninc = cninc;
        this.emial = emial;
        this.latitude = latitude;
        this.longitude = longitude;
        this.company_name = company_name;
        this.vaccine_name = vaccine_name;
        this.vaccine_quality = vaccine_quality;
        this.vaccine_type = vaccine_type;
        this.vaccine_id = vaccine_id;
        this.workerCnic = workerCnic;
        this.vaccination_date = vaccination_date;
    }

    public String getFathername() {
        return fathername;
    }

    public String getMothername() {
        return mothername;
    }

    public String getChildname() {
        return childname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getAddress() {
        return address;
    }

    public String getChildgender() {
        return childgender;
    }

    public String getDateofBirth() {
        return dateofBirth;
    }

    public String getbForm() {
        return bForm;
    }

    public String getCninc() {
        return cninc;
    }

    public String getEmial() {
        return emial;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }

    public String getVaccine_quality() {
        return vaccine_quality;
    }

    public String getVaccine_type() {
        return vaccine_type;
    }

    public String getVaccination_date() {
        return vaccination_date;
    }

    public String getVaccine_id() {
        return vaccine_id;
    }

    public String getWorkerCnic() {
        return workerCnic;
    }
}
