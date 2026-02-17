package com.example.matrimony.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlogCommentRepository extends JpaRepository<BlogComment,Long>{

    List<BlogComment> findByBlogIdOrderByCreatedAtDesc(Long blogId);
    void deleteByBlogId(Long blogId);

}
