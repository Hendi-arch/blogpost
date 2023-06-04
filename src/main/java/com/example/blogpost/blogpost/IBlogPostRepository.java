package com.example.blogpost.blogpost;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBlogPostRepository extends JpaRepository<BlogPostEntity, Integer> {

    Page<BlogPostEntity> findByUserId(Integer userId, Pageable pageable);

}
