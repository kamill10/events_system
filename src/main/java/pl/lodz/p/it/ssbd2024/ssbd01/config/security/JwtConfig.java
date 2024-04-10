package pl.lodz.p.it.ssbd2024.ssbd01.config.security;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Configuration;


@Configuration
@PropertySource("classpath:app.properties")
public class JwtConfig {
}
