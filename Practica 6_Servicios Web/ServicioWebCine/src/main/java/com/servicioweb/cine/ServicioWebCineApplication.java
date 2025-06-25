package com.servicioweb.cine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // Habilita la ejecución de tareas programadas

public class ServicioWebCineApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicioWebCineApplication.class, args);
	}

}
