package pl.lodz.p.it.ssbd2024.ssbd01.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoryconfig.AdminEntityManagerFactoryConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoryconfig.AuthEntityManagerFactoryConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoryconfig.MokEntityManagerFactoryConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoryconfig.MowEntityManagerFactoryConfig;

@Configuration
@Import({
        AdminEntityManagerFactoryConfig.class,
        MokEntityManagerFactoryConfig.class,
        AuthEntityManagerFactoryConfig.class,
        MowEntityManagerFactoryConfig.class,
        AtomikosConfig.class
})
public class JpaConfig {
}
