package com.lowworker.android.model.entities;

/**
 * Created by lowworker on 2015/4/18.
 */
public class Notification {
    private String beacon_id;
    private String  beacon_uuid;
    private String beacon_name;
    private String card_id;
    private String title;
    private String body;

    public String getBeacon_id() {
        return beacon_id;
    }

    public void setBeacon_id(String beacon_id) {
        this.beacon_id = beacon_id;
    }

    public String getBeacon_name() {
        return beacon_name;
    }

    public void setBeacon_name(String beacon_name) {
        this.beacon_name = beacon_name;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "beacon_id='" + beacon_id + '\'' +
                ", beacon_uuid='" + beacon_uuid + '\'' +
                ", beacon_name='" + beacon_name + '\'' +
                ", card_id='" + card_id + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    public String getBeacon_uuid() {
        return beacon_uuid;
    }

    public void setBeacon_uuid(String beacon_uuid) {
        this.beacon_uuid = beacon_uuid;
    }
}
