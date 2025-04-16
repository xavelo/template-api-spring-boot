package com.xavelo.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableDiscoveryClient
@PropertySource("classpath:git.properties")
public class TemplateApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemplateApiApplication.class, args);
	}

}
