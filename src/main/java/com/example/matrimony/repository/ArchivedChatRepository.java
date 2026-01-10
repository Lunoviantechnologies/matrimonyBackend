package com.example.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.matrimony.entity.ArchivedChatMessage;

public interface ArchivedChatRepository
extends JpaRepository<ArchivedChatMessage, Long> {
}
