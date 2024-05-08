package pl.lodz.p.it.ssbd2024.ssbd01.config.security;


import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;
import java.util.Base64;

@Configuration
public class KeyGenerator {
    private static final int KEY_LENGTH_BYTES = 32;
    private static final String SECRET_KEY;

    private static final String ETAG_SECRET_KEY;

    static {
        SECRET_KEY = generateSecretKey();
        ETAG_SECRET_KEY = generateSecretKey();
    }

    public static String getSecretKey() {
        return SECRET_KEY;
    }

    public static String getEtagSecretKey() {
        return ETAG_SECRET_KEY;
    }

    private static String generateSecretKey() {
        byte[] randomBytes = new byte[KEY_LENGTH_BYTES];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }
}
