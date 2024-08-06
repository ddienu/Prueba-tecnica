package com.diego.nunez.Prueba.Tecnica.service.Impl;

import com.diego.nunez.Prueba.Tecnica.dto.UserDto;
import com.diego.nunez.Prueba.Tecnica.entity.Users;
import com.diego.nunez.Prueba.Tecnica.repository.IUserRepository;
import com.diego.nunez.Prueba.Tecnica.service.IJwtService;
import com.diego.nunez.Prueba.Tecnica.service.auth.CustomUserDetailsService;
import com.mysql.cj.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
@Service
public class JwtServiceImpl implements IJwtService {

    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    private final IUserRepository userRepository;

    @Autowired
    public JwtServiceImpl(IUserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public String getToken(UserDto user) {
        return getToken(new HashMap<>(), user);
    }

    private String getToken(Map<String, Object> extraClaims, UserDto user) {
        Optional<Users> userFounded = Optional.ofNullable(userRepository.getUserByEmail(user.getEmail()).orElseThrow(() ->
                new BadCredentialsException("User not found")));
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userFounded.get().getEmail())
                .claim("role", userFounded.get().getRole())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getEmailFromToken(String token) {

        return getAllClaims(token).get("sub", String.class);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getEmailFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

    private Claims getAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpirationDate(String token){
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return getExpirationDate(token).before(new Date());
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (!StringUtils.isNullOrEmpty(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

}
