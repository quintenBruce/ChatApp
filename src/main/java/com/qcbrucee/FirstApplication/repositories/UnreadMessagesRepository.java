package com.qcbrucee.FirstApplication.repositories;

import com.qcbrucee.FirstApplication.models.UnreadMessage;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UnreadMessagesRepository extends MongoRepository<UnreadMessage, ObjectId> {
    List<UnreadMessage> findByUserIdAndConversationId(ObjectId userId, ObjectId conversationId);
    List<UnreadMessage> findByUserId(ObjectId userId);
}
