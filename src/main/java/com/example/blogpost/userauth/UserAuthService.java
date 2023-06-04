package com.example.blogpost.userauth;

import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.blogpost.auth.MyUserDetailService;
import com.example.blogpost.auth.utils.JwtUtilities;
import com.example.blogpost.response.BlogPostResponse;
import com.example.blogpost.user.IUserRepository;
import com.example.blogpost.user.UserEntity;

@Service
public class UserAuthService {

    private final IUserRepository iUserRepository;
    private final IUserAuthRepository iUserAuthRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilities jwtUtilities;
    private final MyUserDetailService myUserDetailService;

    public UserAuthService(IUserRepository iUserRepository, IUserAuthRepository iUserAuthRepository,
            AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtUtilities jwtUtilities,
            MyUserDetailService myUserDetailService) {
        this.iUserRepository = iUserRepository;
        this.iUserAuthRepository = iUserAuthRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtilities = jwtUtilities;
        this.myUserDetailService = myUserDetailService;
    }

    public BlogPostResponse signup(UserEntity userSignupRequest) {
        try {
            String username = userSignupRequest.getUsername();
            String password = userSignupRequest.getPassword();

            UserEntity user = iUserRepository.findByUsername(username);
            if (user != null) {
                return BlogPostResponse.buildBadRequest("Username is already exist");
            }

            UserEntity signupUser = iUserRepository.save(UserEntity.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .build());

            UserDetails userDetails = myUserDetailService.loadUserByUsername(username);
            String jwtToken = jwtUtilities.generateToken(userDetails);
            Date expiredDate = jwtUtilities.extractExpiration(jwtToken);

            iUserAuthRepository.save(UserAuthEntity.builder()
                    .token(jwtToken)
                    .expiredDate(expiredDate)
                    .username(username)
                    .build());

            return BlogPostResponse.buildCreated(signupUser.getId(), signupUser.getUsername(), jwtToken);
        } catch (AuthenticationException e) {
            return BlogPostResponse.buildBadRequest("Invalid username/password");
        }
    }

    public BlogPostResponse signin(UserEntity userSigninRequest) {
        try {
            String username = userSigninRequest.getUsername();
            String password = userSigninRequest.getPassword();

            UserDetails userDetails = myUserDetailService.loadUserByUsername(username);
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String jwtToken = jwtUtilities.generateToken(userDetails);
            Date expiredDate = jwtUtilities.extractExpiration(jwtToken);

            Integer userId = iUserRepository.findIdByUsername(username);
            UserAuthEntity authenticatedUser = iUserAuthRepository.save(UserAuthEntity.builder()
                    .token(jwtToken)
                    .expiredDate(expiredDate)
                    .username(username)
                    .build());

            return BlogPostResponse.buildOk(userId, authenticatedUser.getUsername(),
                    authenticatedUser.getToken());
        } catch (AuthenticationException e) {
            return BlogPostResponse.buildBadRequest(e.getMessage());
        }
    }

    public BlogPostResponse logout(String bearer) {
        try {
            String jwtToken = jwtUtilities.extractBearer(bearer);
            if (jwtToken == null) {
                return BlogPostResponse.buildBadRequest("Invalid token");
            }
            String username = jwtUtilities.extractUser(jwtToken);

            UserAuthEntity userTokenData = iUserAuthRepository.findByUsernameAndToken(username, jwtToken);
            userTokenData.setActive(false);
            iUserAuthRepository.save(userTokenData);

            return BlogPostResponse.buildNoContent();
        } catch (AuthenticationException e) {
            return BlogPostResponse.buildBadRequest("Invalid username/password");
        }
    }

}
