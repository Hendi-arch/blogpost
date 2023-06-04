package com.example.blogpost.response;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

import com.example.blogpost.blogpost.BlogPostEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(value = Include.NON_NULL)
public class BlogPostResponse {

    private int statusCode;

    private String message;

    private Map<String, Object> infoData;

    private List<BlogPostEntity> data;

    private String token;

    private Integer userId;

    private String username;

    private static BlogPostResponse build(int statusCode, String message, Map<String, Object> infoData,
            List<BlogPostEntity> data, String token, Integer userId, String username) {
        return BlogPostResponse.builder()
                .statusCode(statusCode)
                .message(message)
                .infoData(infoData)
                .data(data)
                .token(token)
                .userId(userId)
                .username(username)
                .build();
    }

    private static BlogPostResponse build(int statusCode, String message, List<BlogPostEntity> data) {
        return build(statusCode, message, null, data, null, null, null);
    }

    private static BlogPostResponse build(int statusCode, String message) {
        return build(statusCode, message, null, null, null, null, null);
    }

    private static BlogPostResponse build(int statusCode, String message, Map<String, Object> infoData) {
        return build(statusCode, message, infoData, null, null, null, null);
    }

    public static BlogPostResponse buildOk(List<BlogPostEntity> data) {
        return build(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    public static BlogPostResponse buildOk(List<BlogPostEntity> data, Map<String, Object> infoData) {
        return build(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), infoData, data, null, null, null);
    }

    public static BlogPostResponse buildOk(Integer userId, String username, String token) {
        return build(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null, null, token, userId, username);
    }

    public static BlogPostResponse buildCreated(BlogPostEntity data) {
        return build(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), List.of(data));
    }

    public static BlogPostResponse buildCreated(Integer userId, String username, String token) {
        return build(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), null, null, token, userId,
                username);
    }

    public static BlogPostResponse buildNotFound() {
        return build(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    public static BlogPostResponse buildNotFound(Map<String, Object> infoData) {
        return build(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), infoData);
    }

    public static BlogPostResponse buildNoContent() {
        return build(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.getReasonPhrase());
    }

    public static BlogPostResponse buildBadRequest(String message) {
        return build(HttpStatus.BAD_REQUEST.value(), message);
    }

}
