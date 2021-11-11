package com.boki.realworld.config;

import com.boki.realworld.api.user.domain.User;
import com.boki.realworld.common.dto.UserToken;
import com.boki.realworld.common.exception.jwt.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenProvider {

    public static final String HEADER_PREFIX = "Token ";

    @Value("${jwt.secret-key}")
    private String key;

    @Value("${jwt.token-validity-in-hours}")
    private Integer validity;

    public String generate(String email) {
        LocalDateTime currentTime = LocalDateTime.now();
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
            .setExpiration(
                Date.from(
//                    currentTime.plusMinutes(validity).atZone(ZoneId.systemDefault()).toInstant()))
                    currentTime.plusHours(validity).atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(SignatureAlgorithm.HS512, key)
            .compact();
    }

    public String generateFrom(User user) {
        LocalDateTime currentTime = LocalDateTime.now();
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());
        claims.put("password", user.getPassword());
        claims.put("bio", user.getBio());
        claims.put("image", user.getImage());
        return Jwts.builder()
            .setSubject(user.getEmail())
            .setClaims(claims)
            .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
            .setExpiration(
                Date.from(
                    currentTime.plusHours(validity).atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(SignatureAlgorithm.HS512, key)
            .compact();
    }

    public Optional<String> extract(String header) {
        if (header == null) {
            return Optional.empty();
        }

        if (header.length() < HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size.");
        }

        return Optional.of(header.substring(HEADER_PREFIX.length()));
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public UserToken getUserTokenFrom(String token) {
        Claims claims = getClaims(token);
        return UserToken.builder()
            .id(claims.get("id", Long.class))
            .email(claims.get("email", String.class))
            .username(claims.get("username", String.class))
            .token(token)
            .bio(claims.get("bio", String.class))
            .image(claims.get("image", String.class))
            .build();
    }

    public String parseSubject(String token) {
//        return parseClaims(token).getBody().getSubject(); // setSubject만 하면 가능하지만 setClaims도 한 경우엔 값이 사라진다
        return Optional.ofNullable(parseClaims(token).getBody().getSubject())
            .orElseGet(() -> parseClaims(token).getBody().get("email").toString());
    }

    private Jws<Claims> parseClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException
            | IllegalArgumentException e) {
            throw new BadCredentialsException("Invalid Json Web Token");
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Expired Json Web Token");
        }
    }
}