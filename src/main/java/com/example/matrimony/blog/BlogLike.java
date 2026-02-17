package com.example.matrimony.blog;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="blog_likes",
 uniqueConstraints=@UniqueConstraint(columnNames={"blog_id","username"}))
@NoArgsConstructor
@AllArgsConstructor
public class BlogLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @ManyToOne
    @JoinColumn(name="blog_id")
    private Blog blog;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Blog getBlog() {
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}
    
}
