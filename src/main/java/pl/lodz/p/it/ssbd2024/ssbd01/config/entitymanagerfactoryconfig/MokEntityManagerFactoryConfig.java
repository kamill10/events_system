package pl.lodz.p.it.ssbd2024.ssbd01.config.entitymanagerfactoryconfig;

import com.atomikos.jdbc.AtomikosNonXADataSourceBean;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
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
        basePackages = "pl.lodz.p.it.ssbd2024.ssbd01.mok.repository",
        entityManagerFactoryRef = "mokEntityManagerFactory",
        transactionManagerRef = "transactionManager"
)
@RequiredArgsConstructor
public class MokEntityManagerFactoryConfig {

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

    @Bean(name = "mokEntityManagerFactory")
    public EntityManagerFactory mokEntityManagerFactory() {
        AtomikosNonXADataSourceBean dataSource = new AtomikosNonXADataSourceBean();
        dataSource.setUniqueResourceName("mok");
        dataSource.setUrl(env.getProperty("jdbc.url"));
        dataSource.setUser(env.getProperty("jdbc.mok.user"));
        dataSource.setPassword(env.getProperty("jdbc.mok.password"));
        dataSource.setDriverClassName(Objects.requireNonNull(env.getProperty("jdbc.driverClassName")));
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