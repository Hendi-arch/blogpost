package com.example.blogpost.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.blogpost.user.IUserRepository;
import com.example.blogpost.user.UserEntity;

@Component
public class MyUserDetailService implements UserDetailsService {

    private IUserRepository iUserRepository;

    public MyUserDetailService(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = iUserRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException("Invalid username/password");

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole()));

        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }

}
