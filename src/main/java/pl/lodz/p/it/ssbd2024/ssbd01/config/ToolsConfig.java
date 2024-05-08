package pl.lodz.p.it.ssbd2024.ssbd01.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan(basePackages = {"pl.lodz.p.it.ssbd2024.ssbd01.util"})
@Import({MailConfig.class, I18nConfig.class})
@EnableAspectJAutoProxy
@EnableScheduling
public class ToolsConfig {
}
