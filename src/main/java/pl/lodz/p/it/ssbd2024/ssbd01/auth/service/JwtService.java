package pl.lodz.p.it.ssbd2024.ssbd01.auth.service;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
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
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.KeyGenerator;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.JWTWhitelistToken;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.abstract_exception.AppException;
import pl.lodz.p.it.ssbd2024.ssbd01.exception.mok.AccountLockedException;
import pl.lodz.p.it.ssbd2024.ssbd01.util.JWTUtils;
import pl.lodz.p.it.ssbd2024.ssbd01.util.messages.ExceptionMessages;

import java.util.*;


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

    @PreAuthorize("permitAll()")
    @Transactional(propagation = Propagation.MANDATORY)
    public String generateToken(Map<String, Object> claims, Account account) throws AppException {
        if (!account.getNonLocked()) {
            throw new AccountLockedException(ExceptionMessages.ACCOUNT_LOCKED);
        }

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
                .signWith(JWTUtils.getSecretKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
        jwtWhitelistRepository.saveAndFlush(new JWTWhitelistToken(token, JWTUtils.extractExpiration(token, SECRET_KEY), account));
        return token;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class}, timeoutString = "${transaction.timeout}")
    public void authenticate(String login, String token, HttpServletRequest request) {
        Account account = (Account) userDetailsService.loadUserByUsername(login);

        if (jwtWhitelistRepository.existsByToken(token) && JWTUtils.isTokenValid(token, account, SECRET_KEY)) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());

            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }
}
