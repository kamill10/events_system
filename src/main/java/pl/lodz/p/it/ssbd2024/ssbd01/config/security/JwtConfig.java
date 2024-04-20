package pl.lodz.p.it.ssbd2024.ssbd01.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:app.properties")
public class JwtConfig {
}
