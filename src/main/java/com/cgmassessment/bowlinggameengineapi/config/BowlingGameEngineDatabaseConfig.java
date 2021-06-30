package com.cgmassessment.bowlinggameengineapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.cgmassessment.bowlinggameengineapi.repository",
    entityManagerFactoryRef = "bowlingGameEngineEntityManager",
    transactionManagerRef = "bowlingGameEngineTransactionManager")
public class BowlingGameEngineDatabaseConfig {

  @Autowired private Environment env;

  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean bowlingGameEngineEntityManager() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(bowlingGameEngineDataSource());
    em.setPackagesToScan(new String[] {"com.cgmassessment.bowlinggameengineapi.domain"});

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    em.setJpaVendorAdapter(vendorAdapter);
    HashMap<String, Object> properties = new HashMap<>();
    properties.put(
        "hibernate.dialect",
        env.getProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"));
    em.setJpaPropertyMap(properties);

    return em;
  }

  @Primary
  @Bean
  public DataSource bowlingGameEngineDataSource() {

    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
    dataSource.setUrl(env.getProperty("spring.datasource.url"));
    dataSource.setUsername(env.getProperty("spring.datasource.username"));
    dataSource.setPassword(env.getProperty("spring.datasource.password"));

    return dataSource;
  }

  @Primary
  @Bean
  public PlatformTransactionManager bowlingGameEngineTransactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(bowlingGameEngineEntityManager().getObject());
    return transactionManager;
  }
  
}