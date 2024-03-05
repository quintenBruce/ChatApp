package com.qcbrucee.FirstApplication.services;

import com.qcbrucee.FirstApplication.dtos.ConversationDTO;
import com.qcbrucee.FirstApplication.models.Message;
import org.bson.types.ObjectId;

import java.util.concurrent.CompletableFuture;

public interface UnreadMessageService {


    CompletableFuture<String> PostUnreadMessages(Message message);
}
