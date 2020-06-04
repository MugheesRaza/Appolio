package com.example.appolio.model;

public class register_worker_model {
    String cnic,email,gender,name,password,phone_no,repeat_password,role,status;


    public register_worker_model() {
    }

    public register_worker_model(String cnic, String email, String gender, String name, String password, String phone_no, String repeat_password, String role, String status) {
        this.cnic = cnic;
        this.email = email;
        this.gender = gender;
        this.name = name;
        this.password = password;
        this.phone_no = phone_no;
        this.repeat_password = repeat_password;
        this.role = role;
        this.status = status;
    }

    public String getCnic() {
        return cnic;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getRepeat_password() {
        return repeat_password;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }
}
