package com.qcbrucee.FirstApplication.models;

import com.qcbrucee.FirstApplication.dtos.ConversationDTO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.stream.Collectors;

@Document(collection = "conversations")
public class Conversation {
    private ObjectId _id;
    private String name;
    private List<ObjectId> participants;

    public Conversation() {
    }

    public Conversation(ObjectId id, String name, List<ObjectId> participants) {
        this._id = id;
        this.name = name;
        this.participants = participants;
    }

    public Conversation(ConversationDTO conversationDTO) {
        this._id = conversationDTO.getId() != null ? new ObjectId(conversationDTO.getId()) : new ObjectId();
        this.name = conversationDTO.getName();
        this.participants = convertParticipantIds(conversationDTO.getParticipants());
    }

    private static List<ObjectId> convertParticipantIds(List<String> participantIds) {
        if (participantIds != null) {
            return participantIds.stream()
                    .map(ObjectId::new)
                    .collect(Collectors.toList());
        }
        return null;
    }

    public ObjectId get_id() { return _id; }
    public void set_id(ObjectId _id) { this._id = _id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<ObjectId> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ObjectId> participants) {
        this.participants = participants;
    }
}
