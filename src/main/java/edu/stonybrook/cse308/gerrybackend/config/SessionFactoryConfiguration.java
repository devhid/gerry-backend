package edu.stonybrook.cse308.gerrybackend.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

import javax.persistence.EntityManagerFactory;

@Configuration
public class SessionFactoryConfiguration {
//    @Bean
//    public SessionFactory getSessionFactory(EntityManagerFactory emf) {
//        return emf.unwrap(SessionFactory.class);
//    }
}
