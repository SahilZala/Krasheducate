package com.educate.educate;

public class TopicClass {
    String topicid,subjectid,name,description,type,link,time,date,createdby,activation;

    public TopicClass(String topicid, String subjectid, String name, String description, String type, String link, String time, String date, String createdby, String activation) {
        this.topicid = topicid;
        this.subjectid = subjectid;
        this.name = name;
        this.description = description;
        this.type = type;
        this.link = link;
        this.time = time;
        this.date = date;
        this.createdby = createdby;
        this.activation = activation;
    }

    public TopicClass() {
    }

    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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
