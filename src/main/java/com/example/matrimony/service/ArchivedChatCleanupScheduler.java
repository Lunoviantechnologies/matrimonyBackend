package com.example.matrimony.service;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.matrimony.repository.ArchivedChatRepository;

@Component
public class ArchivedChatCleanupScheduler {

    private final ArchivedChatRepository repository;

    public ArchivedChatCleanupScheduler(
            ArchivedChatRepository repository) {
        this.repository = repository;
    }

    // ✅ Runs every day at 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void deleteArchivedChatsAfter30Days() {

        LocalDateTime expiryDate =
                LocalDateTime.now().minusDays(30);

        int deletedCount =
                repository.deleteArchivedChatsOlderThan(expiryDate);

        System.out.println(
            "Archived Chat Cleanup: Deleted "
            + deletedCount
            + " records older than 30 days"
        );
    }
}
