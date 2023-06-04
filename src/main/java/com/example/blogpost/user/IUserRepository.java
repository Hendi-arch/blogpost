package com.example.blogpost.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IUserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUsername(String username);

    @Query(name = "SELECT u.id FROM UserEntity u WHERE u.username = ?1")
    Integer findIdByUsername(String username);

}
