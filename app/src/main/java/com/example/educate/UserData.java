package com.example.educate;

public class UserData {
    String userid,username,mobileno,password,time,date,activation;

    public UserData() {
    }

    public UserData(String userid, String username, String mobileno, String password, String time, String date, String activation) {
        this.userid = userid;
        this.username = username;
        this.mobileno = mobileno;
        this.password = password;
        this.time = time;
        this.date = date;
        this.activation = activation;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }
}
