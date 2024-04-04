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
@RequiredArgsConstructor
/*@EnableJpaRepositories(
        basePackages = "pl.lodz.p.it.ssbd2024.ssbd01.repositories.mok",
        entityManagerFactoryRef = "adminEntityManagerFactory",
        transactionManagerRef = "transactionManager"
)*/
public class DataSourceAdmin {

    private final Environment env;

    public Map<String, String> jpaProperties() {
        Map<String, String> jpaProperties = new HashMap<>();
        jpaProperties.put("jakarta.persistence.exclude-unlisted-classes", "false");
        jpaProperties.put("jakarta.persistence.schema-generation.database.action", "drop-and-create");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        jpaProperties.put("hibernate.show_sql", "true");
        jpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        jpaProperties.put("jakarta.persistence.transactionType", "JTA");
        jpaProperties.put("jakarta.persistence.sql-load-script-source", "sql/init.sql");

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
        // 0 - TRANSACTION_NONE
        // 1 - TRANSACTION_READ_UNCOMMITTED
        // 2 - TRANSACTION_READ_COMMITTED
        // 4 - TRANSACTION_REPEATABLE_READ
        // 8 - TRANSACTION_SERIALIZABLE
        dataSource.setDefaultTransactionIsolation(2);

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
