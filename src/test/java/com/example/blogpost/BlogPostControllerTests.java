package com.example.blogpost;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.blogpost.blogpost.BlogPostController;
import com.example.blogpost.blogpost.BlogPostEntity;
import com.example.blogpost.blogpost.IBlogPostRepository;

@WebMvcTest(BlogPostController.class)
public class BlogPostControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBlogPostRepository iBlogPostRepository;

    @Test
    public void createBlogPosts_ReturnsCreatedStatus() throws Exception {
        // Mock the repository behavior
        when(iBlogPostRepository.save(any(BlogPostEntity.class)))
                .thenReturn(new BlogPostEntity());

        // Send a POST request to the API endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/api/blog-posts/createBlogPosts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test Title\", \"body\":\"Test Body\", \"author\":\"Test Author\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // Verify that the repository method was called
        verify(iBlogPostRepository, times(1)).save(any(BlogPostEntity.class));
    }

    @Test
    public void findAllBlogPosts_ReturnsNotFoundStatusWhenBlogPostsDataIsEmpty() throws Exception {
        // Create a mock of blog post data
        Page<BlogPostEntity> blogPostsData = Page.empty();

        // Mock the repository behavior
        Pageable pageableRequest = PageRequest.of(0, 25, Sort.by(Order.by("id")));
        when(iBlogPostRepository.findAll(pageableRequest)).thenReturn(blogPostsData);

        // Send a GET request to the API endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/api/blog-posts/findAllBlogPosts"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist());

        // Verify that the repository method was called
        verify(iBlogPostRepository, times(1)).findAll(pageableRequest);
    }

    @Test
    public void findAllBlogPosts_ReturnsOkStatusWhenBlogPostsDataIsNotEmpty() throws Exception {
        // Create a mock of blog post data
        List<BlogPostEntity> blogPostsData = Collections.singletonList(BlogPostEntity.builder().build());
        Pageable pageableRequest = PageRequest.of(0, 25, Sort.by(Order.by("id")));
        Page<BlogPostEntity> pageableBlogPostsData = new PageImpl<>(blogPostsData, pageableRequest,
                blogPostsData.size());

        // Mock the repository behavior
        when(iBlogPostRepository.findAll(pageableRequest)).thenReturn(pageableBlogPostsData);

        // Send a GET request to the API endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/api/blog-posts/findAllBlogPosts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(blogPostsData.size()));

        // Verify that the repository method was called
        verify(iBlogPostRepository, times(1)).findAll(pageableRequest);
    }

    @Test
    public void findBlogPostById_ReturnsOkStatusWithBlogPostWhenExistingId() throws Exception {
        // Create a mock blog post
        BlogPostEntity blogPost = BlogPostEntity.builder().id(1).build();

        // Mock the repository behavior
        when(iBlogPostRepository.findById(1)).thenReturn(Optional.of(blogPost));

        // Send a GET request to the API endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/api/blog-posts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(blogPost.getId()));

        // Verify that the repository method was called
        verify(iBlogPostRepository, times(1)).findById(1);
    }

    @Test
    public void findBlogPostById_ReturnsNotFoundStatusWhenNonexistentId() throws Exception {
        // Mock the repository behavior
        when(iBlogPostRepository.findById(1)).thenReturn(Optional.empty());

        // Send a GET request to the API endpoint
        mockMvc.perform(MockMvcRequestBuilders.get("/api/blog-posts/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify that the repository method was called
        verify(iBlogPostRepository, times(1)).findById(1);
    }

    @Test
    public void updateBlogPostById_ReturnsNotFoundStatusWhenNonexistentId() throws Exception {
        // Mock the repository behavior
        int blogPostId = 1;
        when(iBlogPostRepository.findById(blogPostId)).thenReturn(Optional.empty());

        // Send a PUT request to the API endpoint
        mockMvc.perform(MockMvcRequestBuilders.put("/api/blog-posts/{id}", blogPostId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test Title\", \"body\":\"Test Body\", \"author\":\"Test Author\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify that the repository method was called
        verify(iBlogPostRepository, times(1)).findById(blogPostId);
    }

    @Test
    public void updateBlogPostById_ReturnsOkStatusWhenExistingId() throws Exception {
        // Mock the repository behavior
        int blogPostId = 1;
        BlogPostEntity blogPostData = BlogPostEntity.builder()
                .id(blogPostId)
                .build();
        when(iBlogPostRepository.findById(blogPostId)).thenReturn(Optional.of(blogPostData));
        when(iBlogPostRepository.save(blogPostData)).thenReturn(blogPostData);

        // Send a PUT request to the API endpoint
        mockMvc.perform(MockMvcRequestBuilders.put("/api/blog-posts/{id}", blogPostId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test Title\", \"body\":\"Test Body\", \"author\":\"Test Author\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(blogPostId));

        // Verify that the findById(blogPostId) repository method was called
        verify(iBlogPostRepository, times(1)).findById(blogPostId);

        // Verify that the save(blogPostData) repository method was called
        verify(iBlogPostRepository, times(1)).save(blogPostData);
    }

    @Test
    public void deleteBlogPostById_ReturnsNotFoundStatusWhenNonexistentId() throws Exception {
        // Mock the repository behavior
        int blogPostId = 1;
        when(iBlogPostRepository.existsById(blogPostId)).thenReturn(false);

        // Send a DELETE request to the API endpoint
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/blog-posts/{id}", blogPostId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Verify that the repository method was called
        verify(iBlogPostRepository, times(1)).existsById(blogPostId);
    }

    @Test
    public void deleteBlogPostById_ReturnsNoContentStatusWhenExistingId() throws Exception {
        // Mock the repository behavior
        int blogPostId = 1;
        when(iBlogPostRepository.existsById(blogPostId)).thenReturn(true);
        doNothing().when(iBlogPostRepository).deleteById(blogPostId);

        // Send a DELETE request to the API endpoint
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/blog-posts/{id}", blogPostId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        // Verify that the existsById(blogPostId) repository method was called
        verify(iBlogPostRepository, times(1)).existsById(blogPostId);

        // Verify that the deleteById(blogPostId) repository method was called
        verify(iBlogPostRepository, times(1)).deleteById(blogPostId);
        ;
    }

}
