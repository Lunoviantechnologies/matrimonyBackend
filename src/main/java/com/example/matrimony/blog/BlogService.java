package com.example.matrimony.blog;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


import jakarta.transaction.Transactional;

@Service
public class BlogService {

	private final BlogRepository blogRepo;
	private final BlogCommentRepository commentRepo;
	private final BlogLikeRepository likeRepo;

	private final String uploadDir = "uploads/blog-images";

	public BlogService(BlogRepository blogRepo, BlogCommentRepository commentRepo, BlogLikeRepository likeRepo) {
		this.blogRepo = blogRepo;
		this.commentRepo = commentRepo;
		this.likeRepo = likeRepo;
	}

	// ===============================
	// CONVERT IMAGE TO BASE64
	// ===============================
	private String convertToBase64(String imageName) {
		try {
			if (imageName == null || imageName.isBlank())
				return null;

			Path imagePath = Paths.get(uploadDir).resolve(imageName);

			if (Files.exists(imagePath)) {
				byte[] imageBytes = Files.readAllBytes(imagePath);
				return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageBytes);
			}
		} catch (Exception e) {
			System.out.println("Base64 convert error: " + e.getMessage());
		}
		return null;
	}

	// ===============================
	// CREATE BLOG
	// ===============================
	public Blog createBlog(String title, String content, String category, String imageName, String author,
			String keyword) {

		Blog blog = new Blog();
		blog.setTitle(title);
		blog.setContent(content);
		blog.setCategory(category);
		blog.setAuthor(author);
		blog.setImageUrl(imageName);
		blog.setSlug(SlugUtil.toSlug(title));
		blog.setCreatedAt(LocalDateTime.now());
		blog.setStatus("PUBLISHED");
		blog.setViewsCount(0);
		blog.setLikesCount(0);
		blog.setKeyword(keyword);

		return blogRepo.save(blog);
	}

	// ===============================
	// GET BLOG LIST (USER)
	// ===============================
	public Page<Blog> getBlogs(int page) {

		Page<Blog> blogs = blogRepo.findByStatusOrderByCreatedAtDesc("PUBLISHED", PageRequest.of(page, 10));

		blogs.forEach(blog -> blog.setImageUrl(convertToBase64(blog.getImageUrl())));

		return blogs;
	}

	// ===============================
	// GET BY SLUG
	// ===============================
	public Blog getBySlug(String slug) {

		Blog blog = blogRepo.findBySlug(slug).orElseThrow(() -> new RuntimeException("Blog not found"));

		blog.setViewsCount(blog.getViewsCount() + 1);
		blogRepo.save(blog);

		blog.setImageUrl(convertToBase64(blog.getImageUrl()));

		return blog;
	}

	// ===============================
	// LIKE
	// ===============================
	public String like(Long blogId, String username) {

		if (likeRepo.existsByBlogIdAndUsername(blogId, username))
			return "Already liked";

		Blog blog = blogRepo.findById(blogId).orElseThrow(() -> new RuntimeException("Blog not found"));

		BlogLike like = new BlogLike();
		like.setBlog(blog);
		like.setUsername(username);
		likeRepo.save(like);

		blog.setLikesCount(blog.getLikesCount() + 1);
		blogRepo.save(blog);

		return "Liked";
	}

	// ===============================
	// COMMENT
	// ===============================
	public BlogComment comment(Long blogId, String username, String msg) {

		Blog blog = blogRepo.findById(blogId).orElseThrow(() -> new RuntimeException("Blog not found"));

		BlogComment c = new BlogComment();
		c.setBlog(blog);
		c.setUsername(username);
		c.setMessage(msg);

		return commentRepo.save(c);
	}

	public List<BlogComment> getComments(Long blogId) {
		return commentRepo.findByBlogIdOrderByCreatedAtDesc(blogId);
	}

	// ===============================
	// DELETE BLOG
	// ===============================
	@Transactional
	public String deleteBlog(Long blogId) {

		Blog blog = blogRepo.findById(blogId).orElseThrow(() -> new RuntimeException("Blog not found"));

		// delete image
		try {
			if (blog.getImageUrl() != null) {
				Path imgPath = Paths.get(uploadDir).resolve(blog.getImageUrl());
				Files.deleteIfExists(imgPath);
			}
		} catch (Exception e) {
			System.out.println("Image delete error: " + e.getMessage());
		}

		// delete child tables FIRST
		likeRepo.deleteByBlogId(blogId);
		commentRepo.deleteByBlogId(blogId);

		// delete blog
		blogRepo.delete(blog);

		return "Blog deleted successfully";
	}

	// ===============================
	// UPDATE BLOG
	// ===============================
	public Blog updateBlog(Long blogId, String title, String content, String category, String keyword,
			MultipartFile image, String author) throws Exception {

		Blog blog = blogRepo.findById(blogId).orElseThrow(() -> new RuntimeException("Blog not found"));

		blog.setTitle(title);
		blog.setContent(content);
		blog.setCategory(category);
		blog.setAuthor(author);
		blog.setSlug(SlugUtil.toSlug(title));

		if (keyword != null && !keyword.isBlank()) {
		    blog.setKeyword(keyword);
		} 

		if (image != null && !image.isEmpty()) {

			try {
				if (blog.getImageUrl() != null) {
					Path oldPath = Paths.get(uploadDir).resolve(blog.getImageUrl());
					Files.deleteIfExists(oldPath);
				}
			} catch (Exception e) {
			}

			String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
			Path path = Paths.get(uploadDir).resolve(fileName);

			Files.createDirectories(path.getParent());
			Files.write(path, image.getBytes());

			blog.setImageUrl(fileName);
		}

		return blogRepo.save(blog);
	}

	// ===============================
	// ADMIN ALL BLOGS
	// ===============================
	public Page<Blog> getAllBlogsForAdmin(int page) {

		Page<Blog> blogs = blogRepo.findAll(PageRequest.of(page, 10, Sort.by("createdAt").descending()));

		blogs.forEach(blog -> blog.setImageUrl(convertToBase64(blog.getImageUrl())));

		return blogs;
	}

	// ===============================
	// USER BLOGS
	// ===============================
	public Page<Blog> getBlogsByAuthor(String author, int page) {

		Page<Blog> blogs = blogRepo.findByAuthor(author, PageRequest.of(page, 10, Sort.by("createdAt").descending()));

		blogs.forEach(blog -> blog.setImageUrl(convertToBase64(blog.getImageUrl())));

		return blogs;
	}
	public Page<Blog> getAllBlogsForUser(int page){
	    Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
	    return blogRepo.findAll(pageable);
	}
	public Page<Blog> getBlogsForAdmin(int page){
	    Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
	    return blogRepo.findAll(pageable);
	}

	

	
}
