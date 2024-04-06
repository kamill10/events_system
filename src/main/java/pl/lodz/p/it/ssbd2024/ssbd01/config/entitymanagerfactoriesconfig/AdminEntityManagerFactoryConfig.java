package pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoriesconfig;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@PropertySource("classpath:data-access.properties")
@RequiredArgsConstructor
public class AdminEntityManagerFactoryConfig {

    private final Environment env;

    public Map<String, String> jpaProperties() {
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("jakarta.persistence.exclude-unlisted-classes", env.getProperty("jpa.admin.exclude-unlisted-classes"));
        jpaProperties.put("jakarta.persistence.schema-generation.database.action", env.getProperty("jpa.admin.schema-generation"));
        jpaProperties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        jpaProperties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        jpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", env.getProperty("hibernate.use_jdbc_metadata_defaults"));
        jpaProperties.put("jakarta.persistence.transactionType", env.getProperty("jpa.transactionType"));
        jpaProperties.put("jakarta.persistence.sql-load-script-source", env.getProperty("jpa.load-script-source"));

        return jpaProperties;
    }

    @Bean(name = "adminEntityManagerFactory")
    public EntityManagerFactory entityManagerFactory() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.admin.user"));
        dataSource.setPassword(env.getProperty("jdbc.admin.password"));
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("jdbc.driverClassName")));

        dataSource.setInitialSize(1);
        dataSource.setMinIdle(0);
        dataSource.setMaxIdle(0);
        dataSource.setMaxActive(1);

        dataSource.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("ssbd01admin");
        em.setPersistenceProviderClass(org.hibernate.jpa.HibernatePersistenceProvider.class);
        em.setJtaDataSource(dataSource);
        em.setPackagesToScan(
                "pl.lodz.p.it.ssbd2024.ssbd01.entities"
                );
        em.setJpaPropertyMap(jpaProperties());
        em.afterPropertiesSet();
        return em.getObject();
    }

}
