package pl.lodz.p.it.ssbd2024.ssbd01.config.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.lodz.p.it.ssbd2024.ssbd01.auth.repository.JWTWhitelistRepository;
import pl.lodz.p.it.ssbd2024.ssbd01.config.ConfigurationProperties;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.JWTWhitelistToken;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import javax.crypto.SecretKey;
import javax.security.auth.login.AccountLockedException;
import java.util.*;
import java.util.function.Function;


@Service
public class JwtService {

    private final UserDetailsService userDetailsService;

    private final JWTWhitelistRepository jwtWhitelistRepository;
    private final String SECRET_KEY;

    private final ConfigurationProperties config;

    public JwtService(UserDetailsService userDetailsService,
                      JWTWhitelistRepository jwtWhitelistRepository,
                      ConfigurationProperties config) {
        this.userDetailsService = userDetailsService;
        this.SECRET_KEY = KeyGenerator.getSecretKey();
        this.jwtWhitelistRepository = jwtWhitelistRepository;
        this.config = config;
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
                .setExpiration(new Date(System.currentTimeMillis() + config.getJwtExpiration()))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
        jwtWhitelistRepository.save(new JWTWhitelistToken(token, extractExpiration(token), account));
        return token;
    }


    public String generateToken(Account account) throws AccountLockedException {
        if (!account.getNonLocked()) {
            throw new AccountLockedException(ExceptionMessages.ACCOUNT_LOCKED);
        }
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

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public void authenticate(String login, String token, HttpServletRequest request) {
        Account account = (Account) userDetailsService.loadUserByUsername(login);
        if (jwtWhitelistRepository.existsByToken(token) && isTokenValid(token, account)) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }
}
