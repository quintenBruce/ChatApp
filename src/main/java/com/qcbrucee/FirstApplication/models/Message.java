package com.qcbrucee.FirstApplication.models;

import com.qcbrucee.FirstApplication.dtos.MessageDTO;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

@Document(collection = "messages")
public class Message {

    @Id
    private ObjectId id;
    private String content;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;
    private ObjectId senderId;
    private ObjectId conversationId;

    public Message() {

    }
    public Message(ObjectId Id, String content, Date date, ObjectId sender, ObjectId conversationId) {
        this.id = Id;
        this.content = content;
        this.date = date;
        this.senderId = sender;
        this.conversationId = conversationId;
    }

    // Constructor without id (for creating new messages)
    public Message(String content, String ISOdate, String SenderId, String ConversationId) {
        this.content = content;
        this.date = Date.from(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(ISOdate)));

        this.senderId = new ObjectId(SenderId);
        this.conversationId = new ObjectId(ConversationId);
    }

    public Message(MessageDTO messageDTO) {
        this.id = (messageDTO.getId() != null) ? new ObjectId(messageDTO.getId()) : null;
        this.date = messageDTO.getDate();
        this.senderId = new ObjectId(messageDTO.getSenderId());
        this.conversationId = new ObjectId(messageDTO.getConversationId());
        this.content = messageDTO.getContent();
    }

    public ObjectId getId() { return this.id; }
    public void setId(ObjectId Id) { this.id = Id; }

    public String getContent() { return this.content; }
    public void setContent(String content) { this.content = content; }

    public Date getDate() { return this.date; }
    public void setDate(Date date) { this.date = date; }

    public ObjectId getSenderId() { return this.senderId; }
    public void setSenderId(ObjectId senderId) { this.senderId = senderId; }

    public ObjectId getConversationId() { return this.conversationId; }
    public void setConversationId(ObjectId conversationId) { this.conversationId = conversationId; }
}

