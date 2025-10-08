package com.xavelo.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.xavelo.template.adapter.out.http.external.client")
public class TemplateApplication {

        public static void main(String[] args) {
                SpringApplication.run(TemplateApplication.class, args);
        }

}
