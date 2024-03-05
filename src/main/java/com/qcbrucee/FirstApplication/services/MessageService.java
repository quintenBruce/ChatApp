package com.qcbrucee.FirstApplication.services;

import com.qcbrucee.FirstApplication.models.Message;

import java.util.List;

public interface MessageService {
    Integer CreateMessage(Message message);
    List<Message> GetMessages(List<String> ids);
}
