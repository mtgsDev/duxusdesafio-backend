package br.com.duxusdesafio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"br.com.duxusdesafio", "br.com.duxusdesafio.mapper"})

public class DuxusdesafioApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuxusdesafioApplication.class, args);
	}

}
