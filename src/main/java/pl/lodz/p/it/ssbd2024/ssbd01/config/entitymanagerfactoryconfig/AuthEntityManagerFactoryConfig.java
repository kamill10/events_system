package pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoryconfig;


import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.atomikos.jdbc.AtomikosNonXADataSourceBean;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.postgresql.xa.PGXADataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@PropertySource("classpath:data-access.properties")
@EnableJpaRepositories(
        basePackages = "pl.lodz.p.it.ssbd2024.ssbd01.auth.repository",
        entityManagerFactoryRef = "authEntityManagerFactory",
        transactionManagerRef = "transactionManager"
)
@RequiredArgsConstructor
public class AuthEntityManagerFactoryConfig {

    private final Environment env;

    public Map<String, String> jpaProperties() {
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("jakarta.persistence.exclude-unlisted-classes", env.getProperty("jpa.exclude-unlisted-classes"));
        jpaProperties.put("jakarta.persistence.schema-generation.database.action", env.getProperty("jpa.schema-generation"));
        jpaProperties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        jpaProperties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        jpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", env.getProperty("hibernate.use_jdbc_metadata_defaults"));
        jpaProperties.put("jakarta.persistence.transactionType", env.getProperty("jpa.transactionType"));

        return jpaProperties;
    }

    @Bean(name = "authEntityManagerFactory")
    public EntityManagerFactory authEntityManagerFactory() {
        PGXADataSource pgxaDataSource = new PGXADataSource();
        pgxaDataSource.setUrl(env.getProperty("jdbc.url"));
        pgxaDataSource.setUser(env.getProperty("jdbc.auth.user"));
        pgxaDataSource.setPassword(env.getProperty("jdbc.auth.password"));

        AtomikosDataSourceBean dataSource = new AtomikosDataSourceBean();
        dataSource.setXaDataSource(pgxaDataSource);
        dataSource.setUniqueResourceName("auth");
        dataSource.setDefaultIsolationLevel(Connection.TRANSACTION_READ_COMMITTED);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("ssbd01auth");
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
