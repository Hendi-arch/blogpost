package com.example.blogpost.blogpost;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;

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

    private static BlogPostResponse build(int statusCode, String message, Map<String, Object> infoData,
            List<BlogPostEntity> data) {
        return BlogPostResponse.builder()
                .statusCode(statusCode)
                .message(message)
                .infoData(infoData)
                .data(data)
                .build();
    }

    private static BlogPostResponse build(int statusCode, String message, List<BlogPostEntity> data) {
        return build(statusCode, message, null, data);
    }

    private static BlogPostResponse build(int statusCode, String message) {
        return build(statusCode, message, null, null);
    }

    private static BlogPostResponse build(int statusCode, String message, Map<String, Object> infoData) {
        return build(statusCode, message, infoData, null);
    }

    public static BlogPostResponse buildOk(List<BlogPostEntity> data) {
        return build(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    public static BlogPostResponse buildOk(List<BlogPostEntity> data, Map<String, Object> infoData) {
        return build(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), infoData, data);
    }

    public static BlogPostResponse buildCreated(BlogPostEntity data) {
        return build(HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase(), List.of(data));
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

}
