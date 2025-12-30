package com.example.matrimony.repository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.example.matrimony.entity.ChatMessage;
import com.example.matrimony.entity.Profile;
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Page<ChatMessage> findBySender_IdAndReceiver_IdOrReceiver_IdAndSender_IdOrderByTimestampAsc(
            Long senderId, Long receiverId, Long receiverId2, Long senderId2, Pageable pageable);



    //  get full conversation without pagination
    List<ChatMessage> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(
            Profile sender1, Profile receiver1,
            Profile sender2, Profile receiver2
    );

    // Recent messages for global chat or preview
    List<ChatMessage> findTop20ByOrderByTimestampDesc();
    
    @Transactional
    void deleteBySenderIdOrReceiverId(Long senderId, Long receiverId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM ChatMessage cm WHERE cm.sender.id = :profileId OR cm.receiver.id = :profileId")
    void deleteByProfileId(Long profileId);

}
