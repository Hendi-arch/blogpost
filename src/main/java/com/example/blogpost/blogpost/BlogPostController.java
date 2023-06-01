package com.example.blogpost.blogpost;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<BlogPostResponse> createBlogPosts(@Valid @RequestBody BlogPostEntity blogPost) {
        BlogPostEntity createdBlogPost = iBlogPostRepo.save(blogPost);
        return new ResponseEntity<>(BlogPostResponse.buildCreated(createdBlogPost), HttpStatus.CREATED);
    }

    @GetMapping("/findAllBlogPosts")
    public ResponseEntity<BlogPostResponse> findAllBlogPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "id:asc", name = "sort_by") String[] sort) {

        List<Order> orders = BlogPostHelper.configureSortParameters(sort);
        Pageable pageableRequest = PageRequest.of(page, size, Sort.by(orders));
        Page<BlogPostEntity> pageableBlogPostsData = iBlogPostRepo.findAll(pageableRequest);
        List<BlogPostEntity> blogPostsData = pageableBlogPostsData.getContent();
        Map<String, Object> infoData = Map.of("currentPage", pageableBlogPostsData.getNumber(), "totalItems",
                pageableBlogPostsData.getTotalElements(), "totalPages", pageableBlogPostsData.getTotalPages());
        if (blogPostsData.isEmpty()) {
            return new ResponseEntity<>(BlogPostResponse.buildNotFound(infoData), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(BlogPostResponse.buildOk(blogPostsData, infoData), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostResponse> findBlogPostById(@PathVariable Integer id) {
        Optional<BlogPostEntity> blogPostData = iBlogPostRepo.findById(id);
        return blogPostData
                .map(blogPost -> new ResponseEntity<>(BlogPostResponse.buildOk(List.of(blogPost)), HttpStatus.OK))
                .orElse(new ResponseEntity<>(BlogPostResponse.buildNotFound(), HttpStatus.NOT_FOUND));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<BlogPostResponse> updateBlogPostById(@PathVariable Integer id,
            @Valid @RequestBody BlogPostEntity blogPost) {
        Optional<BlogPostEntity> existingBlogPostData = iBlogPostRepo.findById(id);
        if (existingBlogPostData.isEmpty()) {
            return new ResponseEntity<>(BlogPostResponse.buildNotFound(), HttpStatus.NOT_FOUND);
        }

        BlogPostEntity existingBlogPost = existingBlogPostData.get();
        existingBlogPost.setTitle(blogPost.getTitle());
        existingBlogPost.setBody(blogPost.getBody());
        existingBlogPost.setAuthor(blogPost.getAuthor());

        BlogPostEntity updatedBlogPost = iBlogPostRepo.save(existingBlogPost);
        return new ResponseEntity<>(BlogPostResponse.buildOk(List.of(updatedBlogPost)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BlogPostResponse> deleteBlogPostById(@PathVariable Integer id) {
        boolean isBlogPostExist = iBlogPostRepo.existsById(id);

        if (!isBlogPostExist) {
            return new ResponseEntity<>(BlogPostResponse.buildNotFound(), HttpStatus.NOT_FOUND);
        }

        iBlogPostRepo.deleteById(id);
        return new ResponseEntity<>(BlogPostResponse.buildNoContent(), HttpStatus.NO_CONTENT);
    }

}
