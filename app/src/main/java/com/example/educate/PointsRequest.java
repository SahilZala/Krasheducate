package com.example.educate;

public class PointsRequest {
    String reqid,userid,date,time,activation;


    public PointsRequest(String reqid, String userid, String date, String time, String activation) {
        this.reqid = reqid;
        this.userid = userid;
        this.date = date;
        this.time = time;
        this.activation = activation;
    }

    public PointsRequest() {
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }
}

