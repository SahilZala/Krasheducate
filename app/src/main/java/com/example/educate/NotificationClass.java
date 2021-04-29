package com.example.educate;

public class NotificationClass {
    String nid,head,mess,date,time,pushby,activation;

    public NotificationClass(String nid, String head, String mess, String date, String time, String pushby, String activation) {
        this.nid = nid;
        this.head = head;
        this.mess = mess;
        this.date = date;
        this.time = time;
        this.pushby = pushby;
        this.activation = activation;
    }

    public NotificationClass() {
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
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

    public String getPushby() {
        return pushby;
    }

    public void setPushby(String pushby) {
        this.pushby = pushby;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }
}
