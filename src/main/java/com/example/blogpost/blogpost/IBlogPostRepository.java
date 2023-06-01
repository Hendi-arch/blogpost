package com.example.blogpost.blogpost;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IBlogPostRepository extends JpaRepository<BlogPostEntity, Integer> {

}
