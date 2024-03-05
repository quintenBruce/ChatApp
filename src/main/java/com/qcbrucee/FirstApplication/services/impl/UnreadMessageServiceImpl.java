package com.qcbrucee.FirstApplication.services.impl;

import com.qcbrucee.FirstApplication.models.Conversation;
import com.qcbrucee.FirstApplication.models.Message;
import com.qcbrucee.FirstApplication.models.UnreadMessage;
import com.qcbrucee.FirstApplication.repositories.ConversationRepository;
import com.qcbrucee.FirstApplication.repositories.UnreadMessagesRepository;
import com.qcbrucee.FirstApplication.services.ConversationService;
import com.qcbrucee.FirstApplication.services.UnreadMessageService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UnreadMessageServiceImpl implements UnreadMessageService {
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private UnreadMessagesRepository unreadMessagesRepository;
    @Override
    @Async
    public CompletableFuture<String> PostUnreadMessages(Message message) {
        Optional<Conversation> conversation = conversationRepository.findById(message.getConversationId());
        List<ObjectId> participants = conversation.get().getParticipants();
        participants.remove(message.getSenderId());
        for (ObjectId participantId: participants) {
            UnreadMessage unreadMessage = new UnreadMessage(participantId, message.getId(), message.getConversationId(), message.getDate());
            Boolean status = PostUnreadMessage(unreadMessage);
        }
        return CompletableFuture.completedFuture("");
    }
    private boolean PostUnreadMessage(UnreadMessage unreadMessage) {
        try {
            UnreadMessage returnedUnreadMessage = unreadMessagesRepository.insert(unreadMessage);
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }
}
