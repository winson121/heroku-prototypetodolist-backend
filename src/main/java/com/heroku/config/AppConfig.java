package com.heroku.config;

import java.beans.PropertyVetoException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan("com.heroku")
@PropertySource({"classpath:persistence-pgsql.properties"})
public class AppConfig implements WebMvcConfigurer{
	
	@Autowired
	private Environment env;
	
	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Bean
	public DataSource toDoDataSource() {
		
		// create connection pool
		ComboPooledDataSource todoDataSource = new ComboPooledDataSource();
		
		
		try {
			todoDataSource.setDriverClass("org.postgresql.Driver");
		} catch (PropertyVetoException e) {
			throw new RuntimeException(e);
		}
		
		URI dbUri;
		try {
			dbUri = new URI(System.getenv("DATABASE_URL"));
		    String username = dbUri.getUserInfo().split(":")[0];
		    String password = dbUri.getUserInfo().split(":")[1];
		    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";
		    
			// set database connection
			todoDataSource.setJdbcUrl(dbUrl);
			todoDataSource.setUser(username);
			todoDataSource.setPassword(password);
			
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		
		// set connection pool props
		todoDataSource.setInitialPoolSize(4);
		todoDataSource.setMinPoolSize(4);
		todoDataSource.setMaxPoolSize(20);
		todoDataSource.setMaxIdleTime(1000);
		
		return todoDataSource;
	}
	
	private Properties getHibernateProperties() {
		
		// set hibernate properties
		Properties props = new Properties();
		
		props.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		props.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
	
		return props;
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		
		// create session factory
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		
		// set properties
		sessionFactory.setDataSource(toDoDataSource());
		sessionFactory.setPackagesToScan(env.getProperty("hibernate.packagesToScan"));
		sessionFactory.setHibernateProperties(getHibernateProperties());
		
		return sessionFactory;
	}
	
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		
		// setup transaction manager based on session factory
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		
		logger.info("===Successfully read the datasource and returns the transaction manager===");
		return txManager;
	}
}
