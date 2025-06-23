package com.ecomm.Project.Payload;

public class APIResponse {
    public String message;
    public boolean status;
    
    public APIResponse() {
    }
    
    public APIResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
}
