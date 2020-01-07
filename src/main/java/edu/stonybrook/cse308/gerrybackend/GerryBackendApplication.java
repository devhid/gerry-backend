package edu.stonybrook.cse308.gerrybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GerryBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GerryBackendApplication.class, args);
    }

}
