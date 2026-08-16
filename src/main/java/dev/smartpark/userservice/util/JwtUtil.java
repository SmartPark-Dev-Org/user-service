package dev.smartpark.userservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${access.token.validity.seconds:3600}")
    private int accessTokenValidityInSeconds;

    @Value("${access.token.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String accessTokenSecret;

    @Value("${refresh.token.validity.seconds:86400}")
    private int refreshTokenValidityInSeconds;

    @Value("${refresh.token.secret:5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437}")
    private String refreshTokenSecret;

    private SecretKey getSigningKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String buildToken(String username, String role, int validity, String secret) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiration = new Date(nowMillis + (validity * 1000L));

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey(secret))
                .compact();
    }

    private String extractUsername(String token, String secret) {
        return Jwts.parser()
                .verifyWith(getSigningKey(secret))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private boolean isTokenValidGeneric(String token, String secret, UserDetails userDetails) {
        final String usernameFromToken = extractUsername(token, secret);
        boolean isUsernameMatch = (usernameFromToken.equals(userDetails.getUsername()));
        boolean isUserActive = userDetails.isEnabled();
        return isUsernameMatch && isUserActive;
    }

    private boolean isTokenMathematicallyValid(String token, String secret) {
        try {
            Jwts.parser().verifyWith(getSigningKey(secret)).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String generateAccessToken(String username, String role) {
        return buildToken(username, role, accessTokenValidityInSeconds, accessTokenSecret);
    }

    public String extractUsernameFromAccessToken(String token) {
        return extractUsername(token, accessTokenSecret);
    }

    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        return isTokenValidGeneric(token, accessTokenSecret, userDetails);
    }

    public String generateRefreshToken(String username, String role) {
        return buildToken(username, role, refreshTokenValidityInSeconds, refreshTokenSecret);
    }

    public String extractUsernameFromRefreshToken(String token) {
        return extractUsername(token, refreshTokenSecret);
    }

    public boolean isRefreshTokenMathematicallyValid(String token) {
        return isTokenMathematicallyValid(token, refreshTokenSecret);
    }
}
