package com.example.educate;

public class History {
    String historyid,userid,changes_in,changes_in_id,message,time,date,activation,address;

    public History() {
    }

    public History(String historyid, String userid, String changes_in, String changes_in_id, String message, String time, String date, String activation, String address) {
        this.historyid = historyid;
        this.userid = userid;
        this.changes_in = changes_in;
        this.changes_in_id = changes_in_id;
        this.message = message;
        this.time = time;
        this.date = date;
        this.activation = activation;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHistoryid() {
        return historyid;
    }

    public void setHistoryid(String historyid) {
        this.historyid = historyid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getChanges_in() {
        return changes_in;
    }

    public void setChanges_in(String changes_in) {
        this.changes_in = changes_in;
    }

    public String getChanges_in_id() {
        return changes_in_id;
    }

    public void setChanges_in_id(String changes_in_id) {
        this.changes_in_id = changes_in_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

