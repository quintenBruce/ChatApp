package com.qcbrucee.FirstApplication.dtos;

import com.qcbrucee.FirstApplication.models.Message;
import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class MessageDTO {
    private String id;
    private String content;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;
    private String senderId;
    private String conversationId;

    public MessageDTO() {

    }

    public MessageDTO(String content, Date date, String senderId, String conversationId) {
        this.content = content;
        this.date = date;
        this.senderId = senderId;
        this.conversationId = conversationId;
    }

    public MessageDTO(String id, String content, Date date, String senderId, String conversationId) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.senderId = senderId;
        this.conversationId = conversationId;
    }

    public MessageDTO(Message message) {
        this.id = (message.getId() != null) ? message.getId().toString() : null;
        this.content = message.getContent();
        this.date = message.getDate();
        this.senderId = (message.getSenderId() != null) ? message.getSenderId().toString() : null;
        this.conversationId = (message.getConversationId() != null) ? message.getConversationId().toString() : null;
    }



    public static MessageDTO MessageToDTO(Message message) {
        return new MessageDTO(
                message.getId().toString(),
                message.getContent(),
                message.getDate(),
                message.getSenderId().toString(),
                message.getConversationId().toString()
        );
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public String getSenderId() { return senderId; }

    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getConversationId() { return conversationId; }

    public void setConversationId(String conversationId) { this.conversationId = conversationId; }


}
