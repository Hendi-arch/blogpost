package com.example.blogpost.userauth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blogpost.response.BlogPostResponse;
import com.example.blogpost.user.UserEntity;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/auth")
public class UserAuthController {

    private final UserAuthService userAuthService;

    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<BlogPostResponse> signin(@Valid @RequestBody UserEntity userSigninRequest) {
        BlogPostResponse signinUser = userAuthService.signin(userSigninRequest);
        return new ResponseEntity<>(signinUser, HttpStatus.valueOf(signinUser.getStatusCode()));
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<BlogPostResponse> signup(@Valid @RequestBody UserEntity userSignupRequest) {
        BlogPostResponse signupUser = userAuthService.signup(userSignupRequest);
        return new ResponseEntity<>(signupUser, HttpStatus.valueOf(signupUser.getStatusCode()));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<BlogPostResponse> logout(@RequestHeader(name = "Authorization") String bearer) {
        BlogPostResponse logoutUser = userAuthService.logout(bearer);
        return new ResponseEntity<>(logoutUser, HttpStatus.valueOf(logoutUser.getStatusCode()));
    }

}
