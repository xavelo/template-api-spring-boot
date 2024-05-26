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
        String commitId = "ccc";
        String branch = "master";

        requests += 1;
        System.out.println("/test 0.0.2 from " + podName + " - " + commitId + " - " + branch);
        return "0.0.2 test world ArgoCD from " + podName + " ==> commitID " + commitId + " - branch " + branch;
    }

}

