package pl.lodz.p.it.ssbd2024.ssbd01.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoriesconfig.AdminEntityManagerFactoryConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoriesconfig.AuthEntityManagerFactoryConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoriesconfig.MokEntityManagerFactoryConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoriesconfig.MowEntityManagerFactoryConfig;

@Configuration
@Import({AdminEntityManagerFactoryConfig.class, MokEntityManagerFactoryConfig.class, AuthEntityManagerFactoryConfig.class,
        MowEntityManagerFactoryConfig.class, AtomikosConfig.class})
public class RootConfig {
}
