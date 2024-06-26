package pl.lodz.p.it.ssbd2024.ssbd01.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {
        "pl.lodz.p.it.ssbd2024.ssbd01.auth.service",
        "pl.lodz.p.it.ssbd2024.ssbd01.mok.service",
        "pl.lodz.p.it.ssbd2024.ssbd01.mow.service"
})
public class BusinessConfig {
}
