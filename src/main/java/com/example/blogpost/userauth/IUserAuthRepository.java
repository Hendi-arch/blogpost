package com.example.blogpost.userauth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserAuthRepository extends JpaRepository<UserAuthEntity, Integer> {

    public UserAuthEntity findByToken(String token);

    public UserAuthEntity findByUsernameAndToken(String username, String token);

}
