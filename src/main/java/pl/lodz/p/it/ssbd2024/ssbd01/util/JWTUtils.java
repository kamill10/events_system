package pl.lodz.p.it.ssbd2024.ssbd01.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import pl.lodz.p.it.ssbd2024.ssbd01.entity.mok.Account;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

public class JWTUtils {

    public static boolean isTokenValid(String token, Account account, String secretKey) {
        final String login = extractLogin(token, secretKey);
        return (login.equals(account.getUsername())) && !isTokenExpired(token, secretKey);
    }

    private static boolean isTokenExpired(String token, String secretKey) {
        return extractExpiration(token, secretKey).before(new Date());
    }

    public static Date extractExpiration(String token, String secretKey) {
        return extractClaim(token, Claims::getExpiration, secretKey);
    }

    public static String extractLogin(String token, String secretKey) {
        return extractClaim(token, Claims::getSubject, secretKey);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String secretKey) {
        final Claims claims = extractAllClaims(token, secretKey);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token, String secretKey) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSecretKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static SecretKey getSecretKey(String secretKeyString) {
        byte[] secretKey = Decoders.BASE64.decode(secretKeyString);
        return Keys.hmacShaKeyFor(secretKey);
    }


}
