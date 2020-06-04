package com.example.appolio.model;

public class vaccine {
    String company_name,vaccine_name ,vaccine_quality,vaccine_type,vaccine_id;

    public vaccine() {
    }

    public vaccine(String company_name, String vaccine_name, String vaccine_quality, String vaccine_type, String vaccine_id) {
        this.company_name = company_name;
        this.vaccine_name = vaccine_name;
        this.vaccine_quality = vaccine_quality;
        this.vaccine_type = vaccine_type;
        this.vaccine_id = vaccine_id;
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

    public String getVaccine_id() {
        return vaccine_id;
    }

    public String getVaccine_type() {
        return vaccine_type;
    }
}
