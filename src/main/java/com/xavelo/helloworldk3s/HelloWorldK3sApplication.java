package com.xavelo.helloworldk3s;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:git.properties")
public class HelloWorldK3sApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloWorldK3sApplication.class, args);
	}

}
