package com.example.etta;

public class Message {
    private String sender;
    private String receiver;
    private String message;
    private String email;
    public Message(String sender,String receiver,String message,String email){
        this.message=message;
        this.receiver =receiver;
        this.sender=sender;
        this.email=email;
    }
    public  Message(){

    }
    public void setMessage(String message) {
        this.message = message;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
