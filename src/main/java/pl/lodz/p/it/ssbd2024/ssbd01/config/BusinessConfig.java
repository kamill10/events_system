package pl.lodz.p.it.ssbd2024.ssbd01.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.JwtService;

@Configuration
@ComponentScan(basePackages = {"pl.lodz.p.it.ssbd2024.ssbd01.auth.service", "pl.lodz.p.it.ssbd2024.ssbd01.mok.service"})
@Import({JpaConfig.class})
public class BusinessConfig {
}
