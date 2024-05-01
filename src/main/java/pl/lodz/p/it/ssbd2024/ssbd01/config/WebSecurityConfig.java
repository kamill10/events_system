package pl.lodz.p.it.ssbd2024.ssbd01.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.*;

@Configuration
@Import({ApplicationConfig.class, JwtService.class, JwtAuthFilter.class, JwtConfig.class, SecurityConfig.class})
public class WebSecurityConfig {
}
