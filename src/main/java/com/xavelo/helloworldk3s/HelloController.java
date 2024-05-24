package com.xavelo.helloworldk3s;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    int requests = 0;

    @GetMapping("/hello")
    public String hello() {
        requests += 1;
        System.out.println("/hello - " + requests);
        return "Hello world ArgoCD!!";
    }

    @GetMapping("/test")
    public String test() {
        return "testingggg";
    }
}

