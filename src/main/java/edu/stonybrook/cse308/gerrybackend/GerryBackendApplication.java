package edu.stonybrook.cse308.gerrybackend;

import edu.stonybrook.cse308.gerrybackend.db.services.PrecinctService;
import edu.stonybrook.cse308.gerrybackend.graph.nodes.PrecinctNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class GerryBackendApplication implements CommandLineRunner {

	@Autowired
	private PrecinctService precinctService;

	public static void main(String[] args) {
		SpringApplication.run(GerryBackendApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Starting Application...");

		System.out.println("Finding all precincts...");
		List<PrecinctNode> precincts = precinctService.listPrecincts();
		precincts.forEach(System.out::println);

		System.out.println("Adding new precinct...");
		PrecinctNode precinct = new PrecinctNode();
		precinctService.add(precinct);

		System.out.println("Finding all precincts...");
		precincts = precinctService.listPrecincts();
		precincts.forEach(System.out::println);

	}

}
