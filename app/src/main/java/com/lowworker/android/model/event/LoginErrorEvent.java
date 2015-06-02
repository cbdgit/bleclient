package com.lowworker.android.model.event;

/**
 * Created by lowworker on 2015/5/14.
 */
public class LoginErrorEvent {

private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LoginErrorEvent(String status) {
        this.status = status;
    }
}
