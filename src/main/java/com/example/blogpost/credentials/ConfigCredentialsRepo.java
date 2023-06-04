package com.example.blogpost.credentials;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigCredentialsRepo extends JpaRepository<ConfigCredentials, Integer> {

    ConfigCredentials findByName(String name);

}
