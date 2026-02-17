package com.example.matrimony.blog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogLikeRepository extends JpaRepository<BlogLike,Long>{

    boolean existsByBlogIdAndUsername(Long blogId,String username);
    void deleteByBlogId(Long blogId);

}
