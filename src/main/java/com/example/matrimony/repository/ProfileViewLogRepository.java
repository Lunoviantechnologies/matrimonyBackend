package com.example.matrimony.repository;

import com.example.matrimony.entity.ProfileViewLog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileViewLogRepository
        extends JpaRepository<ProfileViewLog, Long> {

	 @Modifying(clearAutomatically = true, flushAutomatically = true)
	    @Transactional
	    @Query("""
	        DELETE FROM ProfileViewLog p
	        WHERE p.viewer.id = :userId
	           OR p.profile.id = :userId
	    """)
	    void deleteAllByProfileId(Long userId);

}
