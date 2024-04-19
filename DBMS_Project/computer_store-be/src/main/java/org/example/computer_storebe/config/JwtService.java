package org.example.computer_storebe.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final int SECRET_KEY_LENGTH = 256;
    private static final Key SECRET_KEY = generateSecretKey();

    private static Key generateSecretKey() {
        byte[] secretBytes = new byte[SECRET_KEY_LENGTH / 8];
        try {
            SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            secureRandom.nextBytes(secretBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(secretBytes, HMAC_ALGORITHM);
    }

    private Key getSignInKey() {
        return SECRET_KEY;
    }

    public String extractUsername(String token) {
        return null;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
