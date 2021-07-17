package com.educate.educate;

public class TopicCount {
    String userid,topicid,type;

    public TopicCount(String userid, String topicid, String type) {
        this.userid = userid;
        this.topicid = topicid;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TopicCount() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTopicid() {
        return topicid;
    }

    public void setTopicid(String topicid) {
        this.topicid = topicid;
    }
}
