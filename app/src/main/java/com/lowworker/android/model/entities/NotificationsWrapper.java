package com.lowworker.android.model.entities;

import java.util.List;

/**
 * Created by lowworker on 2015/4/18.
 */
public class NotificationsWrapper {
    private List<Notification> data;


    public NotificationsWrapper(List<Notification> data) {
        this.data = data;
    }

    public List<Notification> getData() {
        return data;
    }
}
