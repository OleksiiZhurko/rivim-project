package com.rivim.webclient.config;

import com.rivim.webclient.model.RoleEntity;
import com.rivim.webclient.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.C3P0_ACQUIRE_INCREMENT;
import static org.hibernate.cfg.AvailableSettings.C3P0_MAX_SIZE;
import static org.hibernate.cfg.AvailableSettings.C3P0_MAX_STATEMENTS;
import static org.hibernate.cfg.AvailableSettings.C3P0_MIN_SIZE;
import static org.hibernate.cfg.AvailableSettings.C3P0_TIMEOUT;
import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.DRIVER;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;
import static org.hibernate.cfg.AvailableSettings.PASS;
import static org.hibernate.cfg.AvailableSettings.SHOW_SQL;
import static org.hibernate.cfg.AvailableSettings.URL;
import static org.hibernate.cfg.AvailableSettings.USER;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:database.properties")
public class AppConfig {

  private final Environment env;

  @Autowired
  public AppConfig(Environment env) {
    this.env = env;
  }

  @Bean
  public RestTemplate restTemplateBean() {
    return new RestTemplate();
  }

  @Bean
  public LocalSessionFactoryBean getSessionFactory() {
    var factoryBean = new LocalSessionFactoryBean();
    Properties props = new Properties();
    // Setting JDBC properties
    props.put(DRIVER, env.getProperty("db.driver"));
    props.put(URL, env.getProperty("db.url"));
    props.put(USER, env.getProperty("db.user"));
    props.put(PASS, env.getProperty("db.password"));
    // Setting Hibernate properties
    props.put(DIALECT, env.getProperty("hibernate.dialect"));
    props.put(SHOW_SQL, env.getProperty("hibernate.show_sql"));
    props.put(HBM2DDL_AUTO, env.getProperty("hibernate.hbm2ddl.auto"));
    // Setting C3P0 properties
    props.put(C3P0_MIN_SIZE, env.getProperty("hibernate.c3p0.min_size"));
    props.put(C3P0_MAX_SIZE, env.getProperty("hibernate.c3p0.max_size"));
    props.put(C3P0_ACQUIRE_INCREMENT,
        env.getProperty("hibernate.c3p0.acquire_increment"));
    props.put(C3P0_TIMEOUT, env.getProperty("hibernate.c3p0.timeout"));
    props.put(C3P0_MAX_STATEMENTS,
        env.getProperty("hibernate.c3p0.max_statements"));

    factoryBean.setHibernateProperties(props);
    factoryBean.setAnnotatedClasses(RoleEntity.class, UserEntity.class);

    return factoryBean;
  }

  @Bean
  public HibernateTransactionManager getTransactionManager() {
    var manager = new HibernateTransactionManager();

    manager.setSessionFactory(getSessionFactory().getObject());

    return manager;
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
