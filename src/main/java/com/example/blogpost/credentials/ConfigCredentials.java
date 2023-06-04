package com.example.blogpost.credentials;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity(name = "config_credentials")
public class ConfigCredentials {

    public static final String DEFAULT_NAME = "default";

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String algorithm;

    @Column(name = "encoded_private_key", nullable = false, length = 2000)
    private String encodedPrivateKey;

    @Column(nullable = false, length = 2000)
    private String encoded;

}
