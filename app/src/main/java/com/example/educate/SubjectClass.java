package com.example.educate;

public class SubjectClass {
    String subjectid,name,description,type,time,date,createdby,ftype,activation;

    public SubjectClass() {
    }

    public SubjectClass(String subjectid, String name, String description, String type, String time, String date, String createdby, String ftype, String activation) {
        this.subjectid = subjectid;
        this.name = name;
        this.description = description;
        this.type = type;
        this.time = time;
        this.date = date;
        this.createdby = createdby;
        this.ftype = ftype;
        this.activation = activation;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }
}
