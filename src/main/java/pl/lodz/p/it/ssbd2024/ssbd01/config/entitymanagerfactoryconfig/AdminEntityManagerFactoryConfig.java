package pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoryconfig;

import com.atomikos.jdbc.AtomikosNonXADataSourceBean;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import pl.lodz.p.it.ssbd2024.ssbd01.config.ConfigurationProperties;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;


@Configuration
@PropertySource("classpath:data-access.properties")
@RequiredArgsConstructor
public class AdminEntityManagerFactoryConfig {

    private final ConfigurationProperties config;

    public Map<String, String> jpaProperties() {
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("jakarta.persistence.exclude-unlisted-classes", config.getJpaAdminExcludeUnlistedClasses());
        jpaProperties.put("jakarta.persistence.schema-generation.database.action", config.getJpaAdminSchemaGeneration());
        jpaProperties.put("hibernate.dialect", config.getHibernateDialect());
        jpaProperties.put("hibernate.show_sql", config.getHibernateShowSql());
        jpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", config.getHibernateUseJdbcMetadataDefaults());
        jpaProperties.put("jakarta.persistence.transactionType", config.getJpaTransactionType());
        jpaProperties.put("jakarta.persistence.sql-load-script-source", config.getJpaLoadScriptSource());

        return jpaProperties;
    }

    @Bean(name = "adminEntityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {
        AtomikosNonXADataSourceBean dataSource = new AtomikosNonXADataSourceBean();
        dataSource.setUniqueResourceName("admin");
        dataSource.setUrl(config.getJdbcUrl());
        dataSource.setUser(config.getJdbcAdminUser());
        dataSource.setPassword(config.getJdbcAdminPassword());
        dataSource.setDriverClassName(config.getJdbcDriverClassName());
        dataSource.setDefaultIsolationLevel(Connection.TRANSACTION_READ_COMMITTED);
        dataSource.setLocalTransactionMode(true);
        dataSource.setMaxPoolSize(config.getJdbcAdminPoolSize());

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("ssbd01admin");
        em.setPersistenceProviderClass(org.hibernate.jpa.HibernatePersistenceProvider.class);
        em.setJtaDataSource(dataSource);
        em.setPackagesToScan(
                "pl.lodz.p.it.ssbd2024.ssbd01.entity"
        );
        em.setJpaPropertyMap(jpaProperties());
        em.afterPropertiesSet();
        return em.getObject();
    }
}