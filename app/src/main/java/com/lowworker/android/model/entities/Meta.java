
package com.lowworker.android.model.entities;


public class Meta {

    private String status_code;


    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    @Override
    public String toString() {
        return "Meta{" +
                "status_code='" + status_code + '\'' +
                '}';
    }
}
