package com.xavelo.helloworldk3s;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${HOSTNAME:unknown}")
    private String podName;

    int requests = 0;

    @GetMapping("/hello")
    public String hello() {
        requests += 1;
        System.out.println("/hello from " + podName + " - " + requests);
        return "Hello world ArgoCD from " + podName;
    }

    @GetMapping("/test")
    public String test() {
        return "testingggg";
    }
}

