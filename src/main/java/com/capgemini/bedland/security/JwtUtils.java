package com.capgemini.bedland.security;

import com.capgemini.bedland.entities.ManagerEntity;
import com.capgemini.bedland.entities.OwnerEntity;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.repositories.OwnerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${application.security.jwt.secret-key}")
    private String jwtSigningKey;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private ManagerRepository managerRepository;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                   .setSigningKey(jwtSigningKey)
                   .parseClaimsJws(token)
                   .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails);
    }

    private String createToken(Map<String, Object> claims, UserDetails userDetails) {
        Optional<OwnerEntity> owner = ownerRepository.findByLogin(userDetails.getUsername());
        Optional<ManagerEntity> manager = managerRepository.findByLogin(userDetails.getUsername());
        try {
            if (Boolean.TRUE.equals(owner.isPresent()
                                            && owner.get().getToken() != null
                                            && !isTokenExpired(owner.get().getToken()))
                    && userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                return owner.get().getToken();
            }
            if (Boolean.TRUE.equals(manager.isPresent()
                                            && manager.get().getToken() != null
                                            && !isTokenExpired(manager.get().getToken()))
                    && userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
                return manager.get().getToken();
            }
        } catch (ExpiredJwtException e) {
            return createNewToken(claims, userDetails);
        }
        return createNewToken(claims, userDetails);
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        String userToken = "";
        if (userDetails.getAuthorities()
                       .contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            userToken = ownerRepository.findByLogin(userDetails.getUsername())
                                       .get()
                                       .getToken();
        }
        if (userDetails.getAuthorities()
                       .contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
            userToken = managerRepository.findByLogin(userDetails.getUsername())
                                         .get()
                                         .getToken();
        }
        if (userDetails.getAuthorities()
                       .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            userToken = token;
        }
        boolean tokenInDB = userToken.equals(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && tokenInDB);
    }

    private String createNewToken(Map<String, Object> claims, UserDetails userDetails) {
        String token = Jwts.builder()
                           .setClaims(claims)
                           .setSubject(userDetails.getUsername())
                           .claim("authorities", userDetails.getAuthorities())
                           .setIssuedAt(new Date(System.currentTimeMillis()))
                           .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15)))
                           .signWith(SignatureAlgorithm.HS256, jwtSigningKey)
                           .compact();
        if (userDetails.getAuthorities()
                       .contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) {
            Optional<ManagerEntity> manager = managerRepository.findByLogin(userDetails.getUsername());
            manager.ifPresent(managerEntity -> {
                managerEntity.setToken(token);
                managerRepository.save(managerEntity);
            });
        }
        if (userDetails.getAuthorities()
                       .contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            Optional<OwnerEntity> owner = ownerRepository.findByLogin(userDetails.getUsername());
            owner.ifPresent(ownerEntity -> {
                ownerEntity.setToken(token);
                ownerRepository.save(ownerEntity);
            });
        }
        return token;
    }

}
