package com.example.immo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImmoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ImmoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
