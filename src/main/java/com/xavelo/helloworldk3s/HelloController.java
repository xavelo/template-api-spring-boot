package com.xavelo.helloworldk3s;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@RestController
public class HelloController {

    @Autowired
    private Environment env;

    @Value("${HOSTNAME:unknown}")
    private String podName;

    int requests = 0;

    @GetMapping("/hello")
    public String hello() {
        String commitId = env.getProperty("git.commit.id");
        requests += 1;
        System.out.println("/hello from " + podName + " - " + requests);
        return "Hello world ArgoCD from " + podName + " - commitId " + commitId;
    }

    @GetMapping("/test")
    public String test() {
        return "testingggg";
    }
}

