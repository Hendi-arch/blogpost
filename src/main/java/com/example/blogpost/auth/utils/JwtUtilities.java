package com.example.blogpost.auth.utils;

import java.security.KeyPair;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.blogpost.userauth.IUserAuthRepository;
import com.example.blogpost.userauth.UserAuthEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtilities {

    @Autowired
    private KeyPair rsaKeyPair;

    @Autowired
    private IUserAuthRepository iUserAuthRepository;

    public String extractUser(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails);
    }

    public boolean removeToken(String token) {
        UserAuthEntity userToken = iUserAuthRepository.findByToken(token);
        userToken.setActive(false);
        iUserAuthRepository.save(userToken);
        return true;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUser(token);
        UserAuthEntity userTokenData = iUserAuthRepository.findByUsernameAndToken(username, token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && userTokenData.isActive());
    }

    public String extractBearer(String bearerToken) {
        if (bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            return null;
        }
    }

    private String createToken(HashMap<String, Object> claims, UserDetails subject) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Date expirationDate = calendar.getTime();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(rsaKeyPair.getPrivate(), SignatureAlgorithm.RS256).compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(rsaKeyPair.getPublic()).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

}
