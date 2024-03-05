package com.qcbrucee.FirstApplication.services.impl;

import com.qcbrucee.FirstApplication.models.Message;
import com.qcbrucee.FirstApplication.repositories.MessageRepository;
import com.qcbrucee.FirstApplication.repositories.UnreadMessagesRepository;
import com.qcbrucee.FirstApplication.services.MessageService;
import com.qcbrucee.FirstApplication.services.UnreadMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private UnreadMessageService unreadMessageService;
    @Override
    public Integer CreateMessage(Message message) {
        message.setId(null);
        try {
            Message returnedMessage = messageRepo.insert(message);
            unreadMessageService.PostUnreadMessages(returnedMessage);
            return 1;
        } catch (Throwable e) {
            return 0;
        }
    }

    @Override
    public List<Message> GetMessages(List<String> ids) {
        return messageRepo.findByIdIn(ids);
    }
}
