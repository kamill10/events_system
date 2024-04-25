package pl.lodz.p.it.ssbd2024.ssbd01.config.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.JWTWhitelistToken;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.JWTWhitelistRepository;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;


@Service
public class JwtService {

    private final JWTWhitelistRepository jwtWhitelistRepository;
    @Value("${jwt.secret}")
    private final String SECRET_KEY;

    public JwtService(@Value("${jwt.secret}") String secret,
                      JWTWhitelistRepository jwtWhitelistRepository) {
        this.SECRET_KEY = secret;
        this.jwtWhitelistRepository = jwtWhitelistRepository;
    }

    public String extractLogin(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractId(String token) {
        return extractClaim(token, Claims::getId);
    }

    public UUID extractIdFromHeader(String token) {
        return UUID.fromString(extractId(token.substring(7)));
    }

    public String extractLoginFromHeader(String token) {
        return extractLogin(token.substring(7));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    private SecretKey getSecretKey() {
        byte[] secretKey = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(secretKey);
    }

    @Transactional(rollbackFor = Exception.class,isolation = Isolation.READ_COMMITTED)
    public String generateToken(Map<String, Object> claims, Account account) {
        var authorities = account.getAuthorities();
        List<String> roles = authorities.stream().map(GrantedAuthority::getAuthority).toList();
        String token = Jwts
                         .builder()
                         .setClaims(claims)
                         .claim("role", roles)
                         .setId(account.getId().toString())
                         .setSubject(account.getUsername())
                         .setIssuedAt(new Date(System.currentTimeMillis()))
                         .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                         .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                         .compact();
        jwtWhitelistRepository.save(new JWTWhitelistToken(token,extractExpiration(token)));
        return token;
    }

    public String generateToken(Account account) {
        return generateToken(new HashMap<>(), account);
    }

    public boolean isTokenValid(String token, Account account) {
        final String login = extractLogin(token);
        return (login.equals(account.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


}
