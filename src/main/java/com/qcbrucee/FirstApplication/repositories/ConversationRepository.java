package com.qcbrucee.FirstApplication.repositories;


import com.qcbrucee.FirstApplication.models.Conversation;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ConversationRepository extends MongoRepository<Conversation, ObjectId> {
    List<Conversation> findByParticipantsContaining(ObjectId participantId);

    boolean existsById(ObjectId id);

}
