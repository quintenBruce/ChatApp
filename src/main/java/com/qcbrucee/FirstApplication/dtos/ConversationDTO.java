package com.qcbrucee.FirstApplication.dtos;

import com.qcbrucee.FirstApplication.models.Conversation;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;


public class ConversationDTO {
    private String _id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private List<String> participants;

    public ConversationDTO() {
    }

    public ConversationDTO(String id, String name, List<String> participants) {
        this._id = id;
        this.name = name;
        this.participants = participants;
    }

    public ConversationDTO(Conversation conversation) {
        List<String> participantIds = convertObjectIdsToStrings(conversation.getParticipants());
        this._id = conversation.get_id().toString();
        this.name = conversation.getName();
        this.participants = participantIds;
    }

    public static Conversation DTOToConversation(ConversationDTO conversationDTO) {
        List<ObjectId> participantIds = convertParticipantIds(conversationDTO.getParticipants());
        return new Conversation(new ObjectId(conversationDTO._id), conversationDTO.name, participantIds);
    }

    private static List<ObjectId> convertParticipantIds(List<String> participantIds) {
        if (participantIds != null) {
            return participantIds.stream()
                    .map(ObjectId::new)
                    .collect(Collectors.toList());
        }
        return null;
    }

    public static ConversationDTO ConversationToDTO(Conversation conversation) {
        List<String> participantIds = convertObjectIdsToStrings(conversation.getParticipants());
        return new ConversationDTO(conversation.get_id().toString(), conversation.getName(), participantIds);
    }

    private static List<String> convertObjectIdsToStrings(List<ObjectId> objectIds) {
        if (objectIds != null) {
            return objectIds.stream()
                    .map(ObjectId::toString)
                    .collect(Collectors.toList());
        }
        return null;
    }

    public String getId() { return _id; }
    public void setId(String id) {
        this._id = id;
    }


}
