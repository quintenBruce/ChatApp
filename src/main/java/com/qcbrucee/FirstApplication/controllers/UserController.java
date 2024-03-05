package com.qcbrucee.FirstApplication.controllers;


import com.qcbrucee.FirstApplication.dtos.UserDTO;
import com.qcbrucee.FirstApplication.helpers.hex;
import com.qcbrucee.FirstApplication.models.UnreadMessage;
import com.qcbrucee.FirstApplication.models.User;
import com.qcbrucee.FirstApplication.repositories.UnreadMessagesRepository;
import com.qcbrucee.FirstApplication.repositories.UserRepository;
import com.qcbrucee.FirstApplication.services.ConversationService;
import com.qcbrucee.FirstApplication.services.UnreadMessageService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private UnreadMessagesRepository unreadMessagesRepository;
    @GetMapping()
    public List<UserDTO> GetUsers(@RequestParam(required = true) List<String> ids) {
        List<User> userList = userRepository.findUserByIdIn(ids);
        List<UserDTO> userDTOList = userList.stream().map(UserDTO::new).collect(Collectors.toList());
        return userDTOList;
    }
    @PostMapping()
    public ResponseEntity<?> CreateUser(UserDTO UserDTO) {
        User receivedUser = new User(UserDTO);
        User returnedUser;
        try {
            returnedUser = userRepository.insert(receivedUser);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Failed to insert data.");
        } catch (Throwable e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(new UserDTO(returnedUser)) ;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> GetUser(@PathVariable(required = true) String id) {
        Optional<User> User = userRepository.findById(new ObjectId(id));
        return User.isPresent() ? ResponseEntity.ok(new UserDTO(User.get())) : ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{userId}/unread-messages/")
    public ResponseEntity<?> GetUnreadMessages(@PathVariable(required = true) String userId, @RequestParam(required = false) String conversationId) {
        if (!hex.isValidObjectIdString(userId) || !userRepository.existsById(new ObjectId(userId)))
            return ResponseEntity.badRequest().build();

        List<UnreadMessage> unreadMessages;
        //user exists
        if (!conversationId.trim().isEmpty() && hex.isValidObjectIdString(conversationId) && conversationService.existsById(new ObjectId(conversationId))) {
            //conversation exist
            unreadMessages = unreadMessagesRepository.findByUserIdAndConversationId(new ObjectId(userId), new ObjectId(conversationId));
            if (unreadMessages.isEmpty())
                return ResponseEntity.noContent().build();
            return ResponseEntity.ok(unreadMessages);
        }
        //conversation is malformed or doesn't exist
        unreadMessages = unreadMessagesRepository.findByUserId(new ObjectId(userId));
        if (unreadMessages.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(unreadMessages);
    }
}
