package com.qcbrucee.FirstApplication.repositories;

import com.qcbrucee.FirstApplication.models.Message;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, ObjectId> {
    //@Query(value = "{ 'content': ?0 }", fields = "{ '_id': 1 }")
    List<Message> findByContent(String content);
    List<Message> findByConversationId(ObjectId id);
    List<Message> findByIdIn(List<String> IdList);

}
