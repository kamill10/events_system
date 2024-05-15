package pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoryconfig;

import com.atomikos.jdbc.AtomikosNonXADataSourceBean;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import pl.lodz.p.it.ssbd2024.ssbd01.config.ConfigurationProperties;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;


@Configuration
@PropertySource("classpath:data-access.properties")
@EnableJpaRepositories(
        basePackages = "pl.lodz.p.it.ssbd2024.ssbd01.mok.repository",
        entityManagerFactoryRef = "mokEntityManagerFactory",
        transactionManagerRef = "transactionManager"
)
@RequiredArgsConstructor
public class MokEntityManagerFactoryConfig {

    private final ConfigurationProperties config;

    public Map<String, String> jpaProperties() {
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("jakarta.persistence.exclude-unlisted-classes", config.getJpaExcludeUnlistedClasses());
        jpaProperties.put("jakarta.persistence.schema-generation.database.action", config.getJpaSchemaGeneration());
        jpaProperties.put("hibernate.dialect", config.getHibernateDialect());
        jpaProperties.put("hibernate.show_sql", config.getHibernateShowSql());
        jpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", config.getHibernateUseJdbcMetadataDefaults());
        jpaProperties.put("jakarta.persistence.transactionType", config.getJpaTransactionType());

        return jpaProperties;
    }

    @Bean(name = "mokEntityManagerFactory")
    public EntityManagerFactory mokEntityManagerFactory() {
        AtomikosNonXADataSourceBean dataSource = new AtomikosNonXADataSourceBean();
        dataSource.setUniqueResourceName("mok");
        dataSource.setUrl(config.getJdbcUrl());
        dataSource.setUser(config.getJdbcMokUser());
        dataSource.setPassword(config.getJdbcMokPassword());
        dataSource.setDriverClassName(config.getJdbcDriverClassName());
        dataSource.setDefaultIsolationLevel(Connection.TRANSACTION_READ_COMMITTED);
        dataSource.setMaxPoolSize(5);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("ssbd01mok");
        em.setPersistenceProviderClass(org.hibernate.jpa.HibernatePersistenceProvider.class);
        em.setJtaDataSource(dataSource);
        em.setPackagesToScan(
                "pl.lodz.p.it.ssbd2024.ssbd01.entity.mok"
        );
        em.setJpaPropertyMap(jpaProperties());
        em.afterPropertiesSet();
        return em.getObject();
    }

}