package edu.stonybrook.cse308.gerrybackend;

import edu.stonybrook.cse308.gerrybackend.db.services.PrecinctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GerryBackendApplication {

    @Autowired
    private PrecinctService precinctService;

    public static void main(String[] args) {
        SpringApplication.run(GerryBackendApplication.class, args);
    }

}
