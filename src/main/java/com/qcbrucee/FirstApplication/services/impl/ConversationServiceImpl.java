package com.qcbrucee.FirstApplication.services.impl;

import com.mongodb.client.result.UpdateResult;
import com.qcbrucee.FirstApplication.dtos.ConversationDTO;
import com.qcbrucee.FirstApplication.helpers.hex;
import com.qcbrucee.FirstApplication.models.Conversation;
import com.qcbrucee.FirstApplication.models.User;
import com.qcbrucee.FirstApplication.repositories.ConversationRepository;
import com.qcbrucee.FirstApplication.repositories.UserRepository;
import com.qcbrucee.FirstApplication.services.ConversationService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ListIterator;
import java.util.stream.Collectors;

@Service
public class ConversationServiceImpl implements ConversationService {
    private final MongoTemplate mongoTemplate;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    @Autowired
    public ConversationServiceImpl(MongoTemplate _mongoTemplate, ConversationRepository _repo, UserRepository _userRepository) {
        this.conversationRepository = _repo;
        this.mongoTemplate = _mongoTemplate;
        this.userRepository = _userRepository;
    }

    @Override
    public int addParticipant(String conversationId, String participantId) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(conversationId)));
        Update update = new Update().addToSet("participants", new ObjectId(participantId));
        FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true);

        UpdateResult updated = mongoTemplate.updateFirst(query, update, Conversation.class);
        return (int) updated.getModifiedCount();
    }

    @Override
    public boolean existsById(ObjectId id) {
        return conversationRepository.existsById(id);
    }

    @Override
    public boolean createConversation(ConversationDTO conversationDTO) {
        ListIterator<String> iterator = conversationDTO.getParticipants().listIterator();

        while (iterator.hasNext()) {
            String userId = iterator.next();
            if (hex.isValidObjectIdString(userId)) {
                if (!userRepository.existsById(new ObjectId(userId)))
                    iterator.remove();
                //
                //valid userId
                //
            }
            else if (!userId.trim().isEmpty() && userRepository.existsByUsername(userId)) {
                User user = userRepository.findByUsername(userId);
                iterator.remove();
                iterator.add(user.getId().toHexString());
                //
                //valid username
                //
            } else {
                iterator.remove();
            }
        }
        if (conversationDTO.getParticipants().isEmpty())
            return false;
        conversationDTO.setParticipants(conversationDTO.getParticipants().stream().distinct().collect(Collectors.toList()));

        Conversation conversation = new Conversation(conversationDTO);


        try {
            conversationRepository.insert(conversation);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
}
