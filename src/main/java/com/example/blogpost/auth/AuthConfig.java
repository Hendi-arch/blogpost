package com.example.blogpost.auth;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.blogpost.credentials.ConfigCredentials;
import com.example.blogpost.credentials.ConfigCredentialsRepo;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;

@Configuration
public class AuthConfig {

    private final ConfigCredentialsRepo configCredentialsRepo;

    public AuthConfig(ConfigCredentialsRepo configCredentialsRepo) {
        this.configCredentialsRepo = configCredentialsRepo;
    }

    @Bean
    public KeyPair rsaKeyPair() throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Get the key entity from the database
        ConfigCredentials keyEntity = configCredentialsRepo.findByName(ConfigCredentials.DEFAULT_NAME);

        // Create a new key pair if the key entity doesn't exist
        if (keyEntity == null) {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(SignatureAlgorithm.RS256.getFamilyName());
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            keyEntity = new ConfigCredentials();
            keyEntity.setName(ConfigCredentials.DEFAULT_NAME);
            keyEntity.setAlgorithm(keyPairGenerator.getAlgorithm());
            keyEntity.setEncoded(Encoders.BASE64.encode(keyPair.getPublic().getEncoded()));
            keyEntity.setEncodedPrivateKey(Encoders.BASE64.encode(keyPair.getPrivate().getEncoded()));
            configCredentialsRepo.save(keyEntity);
        }

        // Get the public and private keys from the key entity
        String algorithm = keyEntity.getAlgorithm();
        byte[] encodedKey = Decoders.BASE64.decode(keyEntity.getEncoded());
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        KeySpec keySpec = new X509EncodedKeySpec(encodedKey);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        PrivateKey privateKey = null; // Set to null by default

        // If private key is also stored in the database, retrieve and set it
        if (keyEntity.getEncodedPrivateKey() != null) {
            keySpec = new PKCS8EncodedKeySpec(Decoders.BASE64.decode(keyEntity.getEncodedPrivateKey()));
            privateKey = keyFactory.generatePrivate(keySpec);
        }

        // Create and return the KeyPair instance
        return new KeyPair(publicKey, privateKey);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
