package com.lowworker.android.model.entities;

/**
 * Created by lowworker on 2015/4/18.
 */
public class UserWrapper {
    private User data;


    public UserWrapper(User data) {
        this.data = data;
    }

    public User getData() {
        return data;
    }
}
