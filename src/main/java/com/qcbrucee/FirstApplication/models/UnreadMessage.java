package com.qcbrucee.FirstApplication.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Document(collection = "unread_messages")
public class UnreadMessage {
    @Id
    private ObjectId id;
    private ObjectId userId;
    private ObjectId messageId;
    private ObjectId conversationId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date timestamp;

    public UnreadMessage() {
    }

    public UnreadMessage(ObjectId userId, ObjectId messageId, ObjectId conversationId, Date timestamp) {
        this.userId = userId;
        this.messageId = messageId;
        this.conversationId = conversationId;
        this.timestamp = timestamp;
    }

    public ObjectId getId() {
        return id;
    }

    public void set_id(ObjectId _id) {
        this.id = _id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getMessageId() {
        return messageId;
    }

    public void setMessageId(ObjectId messageId) {
        this.messageId = messageId;
    }

    public ObjectId getConversationId() {
        return conversationId;
    }

    public void setConversationId(ObjectId conversationId) {
        this.conversationId = conversationId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}
