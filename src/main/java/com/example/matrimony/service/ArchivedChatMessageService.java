package com.example.matrimony.service;

import java.util.List;
import com.example.matrimony.entity.ArchivedChatMessage;

public interface ArchivedChatMessageService {

    List<ArchivedChatMessage> getArchivedChatsBetweenUsers(
            Long senderId, Long receiverId);

    List<ArchivedChatMessage> getArchivedChatsByDeletedUser(Long deletedUserId);
}
