package com.example.blogpost.blogpost;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/blog-posts")
public class BlogPostController {

    private final IBlogPostRepository iBlogPostRepo;

    public BlogPostController(
            IBlogPostRepository iBlogPostRepository) {
        iBlogPostRepo = iBlogPostRepository;
    }

    @PostMapping("/createBlogPosts")
    public ResponseEntity<BlogPostEntity> createBlogPosts(@Valid @RequestBody BlogPostEntity blogPost) {
        BlogPostEntity createdBlogPost = iBlogPostRepo.save(blogPost);
        return new ResponseEntity<>(createdBlogPost, HttpStatus.CREATED);
    }

    @GetMapping("/findAllBlogPosts")
    public ResponseEntity<List<BlogPostEntity>> findAllBlogPosts() {
        return new ResponseEntity<>(iBlogPostRepo.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostEntity> findBlogPostById(@PathVariable Integer id) {
        Optional<BlogPostEntity> blogPostData = iBlogPostRepo.findById(id);
        return blogPostData.map(blogPost -> new ResponseEntity<>(blogPost, HttpStatus.OK))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<BlogPostEntity> updateBlogPostById(@PathVariable Integer id,
            @Valid @RequestBody BlogPostEntity blogPost) {
        Optional<BlogPostEntity> existingBlogPostData = iBlogPostRepo.findById(id);
        if (existingBlogPostData.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        BlogPostEntity existingBlogPost = existingBlogPostData.get();
        existingBlogPost.setTitle(blogPost.getTitle());
        existingBlogPost.setBody(blogPost.getBody());
        existingBlogPost.setAuthor(blogPost.getAuthor());

        BlogPostEntity updatedBlogPost = iBlogPostRepo.save(existingBlogPost);
        return new ResponseEntity<>(updatedBlogPost, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBlogPostById(@PathVariable Integer id) {
        boolean isBlogPostExist = iBlogPostRepo.existsById(id);

        if (!isBlogPostExist) {
            return ResponseEntity.notFound().build();
        }

        iBlogPostRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
