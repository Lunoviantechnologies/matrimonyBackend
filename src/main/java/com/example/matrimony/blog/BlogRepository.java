package com.example.matrimony.blog;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog,Long> {

    Optional<Blog> findBySlug(String slug);

    Page<Blog> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);
    Page<Blog> findByAuthor(String author, Pageable pageable);    

}
