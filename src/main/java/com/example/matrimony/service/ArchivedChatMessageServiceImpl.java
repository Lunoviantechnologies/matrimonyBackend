package com.example.matrimony.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.matrimony.entity.ArchivedChatMessage;
import com.example.matrimony.repository.ArchivedChatRepository;

@Service
public class ArchivedChatMessageServiceImpl 
        implements ArchivedChatMessageService {

    private final ArchivedChatRepository repository;

    public ArchivedChatMessageServiceImpl(
    		ArchivedChatRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ArchivedChatMessage> getArchivedChatsBetweenUsers(
            Long senderId, Long receiverId) {

        return repository
            .findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
                senderId, receiverId,
                receiverId, senderId
            );
    }

    @Override
    public List<ArchivedChatMessage> getArchivedChatsByDeletedUser(
            Long deletedUserId) {
        return repository.findByDeletedUserId(deletedUserId);
    }
}
