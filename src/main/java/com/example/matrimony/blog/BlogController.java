package com.example.matrimony.blog;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class BlogController {

    private final BlogService blogService;

  
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }
    
   
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/blog/create")
    public ResponseEntity<?> createBlog(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String keyword,
            @RequestParam String category,
            @RequestParam MultipartFile image,
            @RequestParam(defaultValue = "Admin") String author
    ) {

        try {

            Path uploadPath = Paths.get("uploads/blog-images");
            Files.createDirectories(uploadPath);

            String fileName = "blog_" + System.currentTimeMillis() + ".jpg";
            Path filePath = uploadPath.resolve(fileName);

                       Files.write(filePath, image.getBytes());

                    Blog blog = blogService.createBlog(
                    title,
                    content,
                    category,
                    fileName,  
                    author,
                    keyword
            );

            return ResponseEntity.ok(Map.of(
                    "message", "Blog created successfully",
                    "blogId", blog.getId(),
                    "imageUrl", "/blog-images/" + fileName
            ));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Image upload failed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/blogs")
    public Page<Blog> getAllBlogs(@RequestParam(defaultValue = "0") int page){
        return blogService.getAllBlogsForAdmin(page);
    }
    
   
    @GetMapping("/blog/{slug}")
    public Blog getBlogBySlug(@PathVariable String slug){
        return blogService.getBySlug(slug);
    }

    
    @PostMapping("/blog/like/{blogId}")
    public String likeBlog(@PathVariable Long blogId,
                           @RequestParam String username){
        return blogService.like(blogId, username);
    }

   
    @PostMapping("/blog/comment/{blogId}")
    public BlogComment commentBlog(@PathVariable Long blogId,
                                   @RequestParam String username,
                                   @RequestParam String message){
        return blogService.comment(blogId, username, message);
    }

    
    @GetMapping("/blog/comments/{blogId}")
    public List<BlogComment> getComments(@PathVariable Long blogId){
        return blogService.getComments(blogId);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/blog/delete/{blogId}")
    public String deleteBlog(@PathVariable Long blogId){
        return blogService.deleteBlog(blogId);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/blog/update/{blogId}")
    public Blog updateBlog(
            @PathVariable Long blogId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String category,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(defaultValue = "Admin") String author
    ) throws Exception {

        return blogService.updateBlog(blogId, title, content, category, image, author);
    }
    
    @GetMapping("/user/blogs")
    public Page<Blog> getUserBlogs(@RequestParam String username,
                                   @RequestParam(defaultValue = "0") int page){
        return blogService.getBlogsByAuthor(username, page);
    }
}
