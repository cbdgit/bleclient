
package com.lowworker.android.model.entities;


public class MetaError {

    private String status_code;
    
    private String error_message;
    private String error_type;

    @Override
    public String toString() {
        return "Meta{" +
                "status_code='" + status_code + '\'' +
                ", error_message='" + error_message + '\'' +
                ", error_type='" + error_type + '\'' +
                '}';
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getError_type() {
        return error_type;
    }

    public void setError_type(String error_type) {
        this.error_type = error_type;
    }
}
