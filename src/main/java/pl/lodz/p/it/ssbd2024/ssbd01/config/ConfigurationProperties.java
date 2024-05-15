package pl.lodz.p.it.ssbd2024.ssbd01.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource("classpath:app.properties"),
        @PropertySource("classpath:data-access.properties")
})
@Getter
public class ConfigurationProperties {

    @Value("${auth.attempts:3}")
    private Integer authAttempts;

    @Value("${auth.lock-time:86400}")
    private Integer authLockTime;

    @Value("${confirmation.token.expiration.hours}")
    private Integer confirmationTokenExpiration;

    @Value("${credential_change.token.expiration.minutes}")
    private Integer credentialChangeTokenExpiration;

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private Integer mailPort;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.driverClassName}")
    private String jdbcDriverClassName;

    @Value("${jdbc.username}")
    private String jdbcUsername;

    @Value("${jdbc.mok.user}")
    private String jdbcMokUser;

    @Value("${jdbc.mok.password}")
    private String jdbcMokPassword;

    @Value("${jdbc.mow.user}")
    private String jdbcMowUser;

    @Value("${jdbc.mow.password}")
    private String jdbcMowPassword;

    @Value("${jdbc.admin.user}")
    private String jdbcAdminUser;

    @Value("${jdbc.admin.password}")
    private String jdbcAdminPassword;

    @Value("${jdbc.auth.user}")
    private String jdbcAuthUser;

    @Value("${jdbc.auth.password}")
    private String jdbcAuthPassword;

    @Value("${jpa.admin.exclude-unlisted-classes}")
    private String jpaAdminExcludeUnlistedClasses;

    @Value("${jpa.admin.schema-generation}")
    private String jpaAdminSchemaGeneration;

    @Value("${jpa.exclude-unlisted-classes}")
    private String jpaExcludeUnlistedClasses;

    @Value("${jpa.schema-generation}")
    private String jpaSchemaGeneration;

    @Value("${jpa.transaction-type}")
    private String jpaTransactionType;

    @Value("${jpa.load-script-source}")
    private String jpaLoadScriptSource;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${hibernate.use_jdbc_metadata_defaults}")
    private String hibernateUseJdbcMetadataDefaults;
}
