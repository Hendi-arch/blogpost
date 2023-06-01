package com.example.blogpost.blogpost;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity(name = "blog_posts")
public class BlogPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotBlank
    @Column(length = 100, nullable = false)
    String title;

    @NotBlank
    @Column(length = 500, nullable = false)
    String body;

    @NotBlank
    @Column(length = 50, nullable = false)
    String author;

}
