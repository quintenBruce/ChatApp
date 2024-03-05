package com.qcbrucee.FirstApplication.controllers;

import com.qcbrucee.FirstApplication.dtos.ConversationDTO;
import com.qcbrucee.FirstApplication.dtos.MessageDTO;
import com.qcbrucee.FirstApplication.helpers.hex;
import com.qcbrucee.FirstApplication.models.Conversation;
import com.qcbrucee.FirstApplication.models.Message;
import com.qcbrucee.FirstApplication.repositories.MessageRepository;
import com.qcbrucee.FirstApplication.services.MessageService;
import com.qcbrucee.FirstApplication.services.UnreadMessageService;
import io.micrometer.common.util.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("messages")
public class MessageController {
    @Autowired
    private MessageRepository repo;
    @Autowired
    private MessageService messageService;
    @GetMapping(path = "")
    public ResponseEntity<?> GetMessages(@RequestParam(required = false) List<String> ids,
                                         @RequestParam(required = false) String conversationId,
                                         @RequestParam(required = false) String sortBy,
                                         @RequestParam(defaultValue = "asc", required = false) String sortOrder) {

        List<Message> li = new ArrayList<>();
        if (ids != null && !ids.isEmpty()) {
            List<String> validIds = new ArrayList<>();
            for (String id: ids) {
                if (hex.isValidObjectIdString(id))
                    validIds.add(id);
            }
            li = messageService.GetMessages(validIds);
        }
        else if (StringUtils.isNotEmpty(conversationId) && hex.isValidObjectIdString(conversationId)) {
            li = repo.findByConversationId(new ObjectId(conversationId));
        } else {
            li = repo.findAll();
        }
        ///////////////////////////////////////////////////
        //////the above else clause should be removed//////
        ///////////////////////////////////////////////////
        if (li.isEmpty())
            return ResponseEntity.noContent().build();

        if (sortBy != null && sortBy.compareToIgnoreCase("date") == 0) {
            if (sortOrder != null && sortOrder.compareToIgnoreCase("desc") == 0)
                li.sort(Comparator.comparing(Message::getDate).reversed());
            else
                li.sort(Comparator.comparing(Message::getDate));
        }
        List<MessageDTO> messageDTOList = li.stream().map(MessageDTO::new).collect(Collectors.toList());

        return ResponseEntity.ok(messageDTOList);
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> GetMessage(@PathVariable(required = true) String id) {
        Optional<Message> message = repo.findById(new ObjectId(id));
        return message.isPresent() ? ResponseEntity.ok(new MessageDTO(message.get())) : ResponseEntity.noContent().build();
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> DeleteMessage(@PathVariable(required = true) String id) {
        if (!hex.isValidObjectIdString(id))
            return ResponseEntity.badRequest().build();
        if (!repo.existsById(new ObjectId(id)))
            return ResponseEntity.notFound().build();

        repo.deleteById(new ObjectId(id));
        return ResponseEntity.ok().build();
    }

    @PostMapping()
    public ResponseEntity<?> CreateMessage(MessageDTO messageDTO) {
        if (!hex.isValidObjectIdString(messageDTO.getSenderId()) || !hex.isValidObjectIdString(messageDTO.getConversationId()))
            return ResponseEntity.badRequest().build();
        Integer status = messageService.CreateMessage(new Message(messageDTO));
        return status == 1 ? ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
    }
}

