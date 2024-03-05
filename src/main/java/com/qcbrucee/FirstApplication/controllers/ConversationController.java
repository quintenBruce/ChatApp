package com.qcbrucee.FirstApplication.controllers;

import com.mongodb.client.MongoClients;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.internal.bulk.UpdateRequest;
import com.qcbrucee.FirstApplication.dtos.MessageDTO;
import com.qcbrucee.FirstApplication.dtos.UserDTO;
import com.qcbrucee.FirstApplication.helpers.hex;
import com.qcbrucee.FirstApplication.models.Conversation;
import com.qcbrucee.FirstApplication.dtos.ConversationDTO;
import com.qcbrucee.FirstApplication.models.Message;
import com.qcbrucee.FirstApplication.models.User;
import com.qcbrucee.FirstApplication.repositories.ConversationRepository;
import com.qcbrucee.FirstApplication.repositories.MessageRepository;
import com.qcbrucee.FirstApplication.repositories.UserRepository;
import com.qcbrucee.FirstApplication.services.ConversationService;
import io.micrometer.common.util.StringUtils;
import jakarta.websocket.server.PathParam;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("conversations")
public class ConversationController {

    @Autowired
    private ConversationRepository repo;
    @Autowired
    private MessageRepository messageRepository;
    private final ConversationService conversationService;
    public ConversationController(ConversationService _conversationService) {
        this.conversationService = _conversationService;
    }


    @GetMapping()
    public ResponseEntity<?> GetConversations(@RequestParam(required = false) String participantId) {
        List<Conversation> conversations = new ArrayList<>();

        if (StringUtils.isNotEmpty(participantId)) {
            conversations = repo.findByParticipantsContaining(new ObjectId(participantId));
        } else {
            conversations = repo.findAll();
        }
        if (conversations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(conversations.stream().map(ConversationDTO::new).collect(Collectors.toList()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> GetConversation(@PathVariable(required = true) String id) {
        Optional<Conversation> conversation = repo.findById(new ObjectId(id));
        return conversation.isPresent() ? ResponseEntity.ok(new ConversationDTO(conversation.get())) : ResponseEntity.noContent().build();
    }
    @PostMapping()
    public ResponseEntity<?> CreateConversation(@RequestBody() ConversationDTO conversationDTO) {
        if (conversationDTO.getParticipants() == null || conversationDTO.getName() == null || conversationDTO.getName().trim().equals("")) {
            return ResponseEntity.badRequest().build();
        }
        conversationDTO.setId(null);
        boolean created = conversationService.createConversation(conversationDTO);

        return created ? ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
    }

    @PostMapping(path = "/{conversationId}/participants")
    public ResponseEntity<?> CreateParticipant(@PathVariable(required = true) String conversationId,
                                               @RequestParam(required = true) String participantId) {

        if (!hex.isValidObjectIdString(conversationId) || !hex.isValidObjectIdString(participantId))
            return new ResponseEntity<>("Malformed Id", HttpStatus.BAD_REQUEST);

        boolean exists = conversationService.existsById(new ObjectId(conversationId));
        if (!exists)
            return new ResponseEntity<>("Conversation Not Found", HttpStatus.NOT_FOUND);


        if (conversationService.addParticipant(conversationId, participantId) > 0)
            return ResponseEntity.ok("Success.");

        return ResponseEntity.internalServerError().build();
    }
    @GetMapping(path = "/{conversationId}/messages")
    public ResponseEntity<?> GetMessages(@PathVariable(required = true) String conversationId,
                                         @RequestParam(required = false) String sortBy,
                                         @RequestParam(defaultValue = "asc", required = false) String sortOrder,
                                         @RequestParam(defaultValue = "false", required = false) String ids_only) {

        if (!hex.isValidObjectIdString(conversationId))
            return new ResponseEntity<>("Malformed Id", HttpStatus.BAD_REQUEST);

        List<Message> li = new ArrayList<>();
        li = messageRepository.findByConversationId(new ObjectId(conversationId));

        if (li.isEmpty())
            return ResponseEntity.noContent().build();

        if (sortBy != null && sortBy.compareToIgnoreCase("date") == 0) {
            if (sortOrder != null && sortOrder.compareToIgnoreCase("desc") == 0)
                li.sort(Comparator.comparing(Message::getDate).reversed());
            else
                li.sort(Comparator.comparing(Message::getDate));
        }
        if (ids_only == null || !ids_only.equalsIgnoreCase("true")) {
            List<MessageDTO> messageDTOList = li.stream().map(MessageDTO::new).collect(Collectors.toList());

            return ResponseEntity.ok(messageDTOList);
        }

        List<String> ids = new ArrayList<>();
        for (Message message: li) {
            ids.add(message.getId().toHexString());
        }
        return ResponseEntity.ok(ids);

    }
    @PutMapping(path = "/{conversationId}/messages")
    public ResponseEntity<?> CreateMessage(@PathVariable(required = true) String conversationId,
                                           @RequestBody MessageDTO messageDTO) {
        if (!hex.isValidObjectIdString(conversationId) || !hex.isValidObjectIdString(messageDTO.getSenderId()))
            return new ResponseEntity<>("Malformed Id", HttpStatus.BAD_REQUEST);

        messageDTO.setId(null);
        if (messageDTO.getContent().length() == 0)
            return ResponseEntity.badRequest().build();
        Message receivedMessage = messageRepository.insert(new Message(messageDTO));
        return ResponseEntity.ok(new MessageDTO(receivedMessage));
    }
}
