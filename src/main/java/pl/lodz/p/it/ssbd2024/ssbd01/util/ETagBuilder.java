package pl.lodz.p.it.ssbd2024.ssbd01.util;

import org.springframework.util.DigestUtils;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.KeyGenerator;

public class ETagBuilder {

    public static String buildETag(String... values) {
        StringBuilder eTag = new StringBuilder();
        for (String value : values) {
            eTag.append(value);
        }
        eTag.append(KeyGenerator.getEtagSecretKey());
        return DigestUtils.md5DigestAsHex(eTag.toString().getBytes());
    }

    public static boolean isETagValid(String eTag, String... values) {
        return eTag.equals(buildETag(values));
    }

}
