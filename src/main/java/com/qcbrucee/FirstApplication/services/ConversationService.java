package com.qcbrucee.FirstApplication.services;

import com.qcbrucee.FirstApplication.dtos.ConversationDTO;
import com.qcbrucee.FirstApplication.models.Conversation;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;


public interface ConversationService {
    public int addParticipant(String conversationId, String participantId);
    public boolean existsById(ObjectId id);
    public boolean createConversation(ConversationDTO conversationDTO);
}
