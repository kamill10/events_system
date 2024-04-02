package pl.lodz.p.it.ssbd2024.ssbd01.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.lodz.p.it.ssbd2024.ssbd01.config.datasource.DataSourceAdmin;
import pl.lodz.p.it.ssbd2024.ssbd01.config.datasource.DataSourceAuth;
import pl.lodz.p.it.ssbd2024.ssbd01.config.datasource.DataSourceMok;
import pl.lodz.p.it.ssbd2024.ssbd01.config.datasource.DataSourceMow;
import pl.lodz.p.it.ssbd2024.ssbd01.repositories.mok.UserRepository;

@Configuration
@Import({DataSourceAdmin.class, DataSourceMok.class, DataSourceAuth.class, DataSourceMow.class })
public class DataSourceConfig {
}
