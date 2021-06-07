package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessagesModel implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("conversation_id")
    private int conversation_id;

    @SerializedName("message")
    private String message;

    @SerializedName("type")
    private String type;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("conversation")
    private ConversationModel conversation;

    public MessagesModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getConversation_id() {
        return conversation_id;
    }

    public void setConversation_id(int conversation_id) {
        this.conversation_id = conversation_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ConversationModel getConversation() {
        return conversation;
    }

    public void setConversation(ConversationModel conversation) {
        this.conversation = conversation;
    }
}
