package com.example.educate;

public class Comment {
    String userid,comment,time,date,activation;

    public Comment(String userid, String comment, String time, String date, String activation) {
        this.userid = userid;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
