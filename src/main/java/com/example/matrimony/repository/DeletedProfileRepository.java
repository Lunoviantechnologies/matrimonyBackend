package com.example.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.matrimony.entity.DeletedProfile;

public interface DeletedProfileRepository
        extends JpaRepository<DeletedProfile, Long> {
}
