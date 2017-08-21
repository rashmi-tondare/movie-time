
package com.assignment.movietime.model;

/**
 * Created by Rashmi on 20/08/17.
 */

public class ChatMessage {
    private String id;
    private String message;
    private String senderName;
    private String senderPhotoUrl;

    public ChatMessage() {}

    public ChatMessage(String message, String senderName, String senderPhotoUrl) {
        this.message = message;
        this.senderName = senderName;
        this.senderPhotoUrl = senderPhotoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhotoUrl() {
        return senderPhotoUrl;
    }

    public void setSenderPhotoUrl(String senderPhotoUrl) {
        this.senderPhotoUrl = senderPhotoUrl;
    }
}
