package pl.lodz.p.it.ssbd2024.ssbd01.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoryconfig.AdminEntityManagerFactoryConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoryconfig.AuthEntityManagerFactoryConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoryconfig.MokEntityManagerFactoryConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoryconfig.MowEntityManagerFactoryConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.ApplicationConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.JwtAuthFilter;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.JwtService;
import pl.lodz.p.it.ssbd2024.ssbd01.config.security.SecurityConfig;

@Configuration
@Import({JpaConfig.class, BusinessConfig.class, ToolsConfig.class})
public class RootConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

}
