package com.yohealth.repository.interfaces;

import com.yohealth.domain.model.Message;
import java.util.Collection;

public interface IMessageRepository {
    int createMessage(Message message);
    Message getMessageByID(Long messageID);
    Collection<Message> getAllMessagesByUserID(Long userID);
    int updateMessage(Message message);

    
}
