package com.xavelo.helloworldk3s;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.GitProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@RestController
public class HelloController {

    @Autowired
    private GitProperties gitProperties;

    @Value("${HOSTNAME:unknown}")
    private String podName;

    int requests = 0;

    @GetMapping("/hello")
    public String hello() {
        String commitId = gitProperties.getShortCommitId();
        String branch = gitProperties.getBranch();

        requests += 1;
        System.out.println("/test from " + podName + " - " + commitId + " - " + branch);
        return "test world ArgoCD from " + podName + " ==> commitID " + commitId + " - branch " + branch;
    }

}

