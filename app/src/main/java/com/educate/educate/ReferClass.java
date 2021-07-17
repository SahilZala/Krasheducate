package com.educate.educate;

public class ReferClass {
    String referid,referbyid,time,date,id;

    public ReferClass(String referid, String referbyid, String time, String date, String id) {
        this.referid = referid;
        this.referbyid = referbyid;
        this.time = time;
        this.date = date;
        this.id = id;
    }

    public ReferClass() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReferid() {
        return referid;
    }

    public void setReferid(String referid) {
        this.referid = referid;
    }

    public String getReferbyid() {
        return referbyid;
    }

    public void setReferbyid(String referbyid) {
        this.referbyid = referbyid;
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
}
