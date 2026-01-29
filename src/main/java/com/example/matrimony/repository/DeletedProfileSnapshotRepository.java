package com.example.matrimony.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.matrimony.entity.DeletedProfileSnapshot;

public interface DeletedProfileSnapshotRepository 
        extends JpaRepository<DeletedProfileSnapshot, Long> {}