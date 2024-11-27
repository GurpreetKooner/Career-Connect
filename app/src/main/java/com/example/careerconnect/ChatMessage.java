package com.example.careerconnect;

public class ChatMessage {
    private String sender;
    private String receiver;
    private String message;
    private long timestamp;

    public ChatMessage() {}

    public ChatMessage(String sender, String receiver, String message, long timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
