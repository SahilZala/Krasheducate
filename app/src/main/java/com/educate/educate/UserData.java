package com.educate.educate;

public class UserData {
    String userid,username,mobileno,password,mail,profile,time,date,points,activation,paidunpaid,referdone,referby;

    public UserData() {
    }

    public UserData(String userid, String username, String mobileno, String password, String mail, String profile, String time, String date, String points, String activation, String paidunpaid, String referdone, String referby) {
        this.userid = userid;
        this.username = username;
        this.mobileno = mobileno;
        this.password = password;
        this.mail = mail;
        this.profile = profile;
        this.time = time;
        this.date = date;
        this.points = points;
        this.activation = activation;
        this.paidunpaid = paidunpaid;
        this.referdone = referdone;
        this.referby = referby;
    }

    public String getPaidunpaid() {
        return paidunpaid;
    }

    public void setPaidunpaid(String paidunpaid) {
        this.paidunpaid = paidunpaid;
    }

    public String getReferby() {
        return referby;
    }

    public void setReferby(String referby) {
        this.referby = referby;
    }

    public String getReferdone() {
        return referdone;
    }

    public void setReferdone(String referdone) {
        this.referdone = referdone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
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

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }
}
