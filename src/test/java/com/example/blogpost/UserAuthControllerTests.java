package com.example.blogpost;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.blogpost.auth.AuthSecurityConfigurer;
import com.example.blogpost.auth.MyUserDetailService;
import com.example.blogpost.auth.utils.JwtUtilities;
import com.example.blogpost.response.BlogPostResponse;
import com.example.blogpost.user.UserEntity;
import com.example.blogpost.userauth.UserAuthController;
import com.example.blogpost.userauth.UserAuthService;

@WebMvcTest(UserAuthController.class)
@MockBeans({
        @MockBean(UserAuthService.class),
        @MockBean(JwtUtilities.class),
        @MockBean(MyUserDetailService.class)
})
@Import(AuthSecurityConfigurer.class)
public class UserAuthControllerTests {

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void signup_ReturnsCreatedStatusWhenSuccess() throws Exception {
        // Mock data
        String username = "testuser";
        String password = "testpassword";
        String userSignupPayload = "{\"username\":\"testuser\", \"password\":\"testpassword\"}";
        UserEntity userSignupRequest = UserEntity.builder()
                .username(username)
                .password(password)
                .build();
        BlogPostResponse expectedResponse = BlogPostResponse.buildCreated(1, username, "token");

        // Mock the behavior of dependencies
        when(userAuthService.signup(userSignupRequest)).thenReturn(expectedResponse);

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userSignupPayload))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("token"));

        // Verify interactions
        verify(userAuthService).signup(userSignupRequest);
    }

    @Test
    public void signup_ReturnsBadRequestStatusWhenFailure() throws Exception {
        // Mock data
        String username = "existinguser";
        String password = "testpassword";
        String userSignupPayload = "{\"username\":\"existinguser\", \"password\":\"testpassword\"}";
        UserEntity userSignupRequest = UserEntity.builder()
                .username(username)
                .password(password)
                .build();
        BlogPostResponse expectedResponse = BlogPostResponse.buildBadRequest("Username is already exist");

        // Mock the behavior of dependencies
        when(userAuthService.signup(userSignupRequest)).thenReturn(expectedResponse);

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userSignupPayload))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Username is already exist"));

        // Verify interactions
        verify(userAuthService).signup(userSignupRequest);
    }

    @Test
    public void signin_ReturnsOkStatusWhenSuccess() throws Exception {
        // Mock data
        String username = "testuser";
        String password = "testpassword";
        String userSigninPayload = "{\"username\":\"testuser\", \"password\":\"testpassword\"}";
        UserEntity userSigninRequest = UserEntity.builder()
                .username(username)
                .password(password)
                .build();
        BlogPostResponse expectedResponse = BlogPostResponse.buildOk(1, username, "token");

        // Mock the behavior of dependencies
        when(userAuthService.signin(userSigninRequest)).thenReturn(expectedResponse);

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userSigninPayload))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("token"));

        // Verify interactions
        verify(userAuthService).signin(userSigninRequest);
    }

    @Test
    public void signin_ReturnsBadRequestStatusWhenFailure() throws Exception {
        // Mock data
        String username = "existinguser";
        String password = "testpassword";
        String userSigninPayload = "{\"username\":\"existinguser\", \"password\":\"testpassword\"}";
        UserEntity userSigninRequest = UserEntity.builder()
                .username(username)
                .password(password)
                .build();
        BlogPostResponse expectedResponse = BlogPostResponse.buildBadRequest("Invalid username/password");

        // Mock the behavior of dependencies
        when(userAuthService.signin(userSigninRequest)).thenReturn(expectedResponse);

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userSigninPayload))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("Invalid username/password"));

        // Verify interactions
        verify(userAuthService).signin(userSigninRequest);
    }

    // TODO: Logout testing

}
