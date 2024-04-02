package pl.lodz.p.it.ssbd2024.ssbd01.config.datasource;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@PropertySource("classpath:data-access.properties")
//@EnableJpaRepositories(
//        basePackages = "com.example.mow.repository",
//        entityManagerFactoryRef = "mowEntityManagerFactory",
//        transactionManagerRef = "transactionManager"
//)
@RequiredArgsConstructor
public class DataSourceMow {

    private final Environment env;

    public Map<String, String> jpaProperties() {
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("jakarta.persistence.exclude-unlisted-classes", "true");
        jpaProperties.put("jakarta.persistence.schema-generation.database.action", "none");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaProperties.put("hibernate.show_sql", "true");
        jpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        jpaProperties.put("jakarta.persistence.transactionType", "JTA");

        return jpaProperties;
    }

    @Bean(name = "mowEntityManagerFactory")
    public EntityManagerFactory mowEntityManagerFactory() {
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUsername(env.getProperty("jdbc.mow.user"));
        dataSource.setPassword(env.getProperty("jdbc.mow.password"));
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("jdbc.driverClassName")));

        // 0 - TRANSACTION_NONE
        // 1 - TRANSACTION_READ_UNCOMMITTED
        // 2 - TRANSACTION_READ_COMMITTED
        // 4 - TRANSACTION_REPEATABLE_READ
        // 8 - TRANSACTION_SERIALIZABLE
        dataSource.setDefaultTransactionIsolation(2);

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("ssbd01mow");
        em.setPersistenceProviderClass(org.hibernate.jpa.HibernatePersistenceProvider.class);
        em.setJtaDataSource(dataSource);
        em.setPackagesToScan(
                "pl.lodz.p.it.ssbd2024"
        );
        em.setJpaPropertyMap(jpaProperties());
        em.afterPropertiesSet();
        return em.getObject();
    }
}
