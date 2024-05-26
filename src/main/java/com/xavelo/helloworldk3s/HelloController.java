package com.xavelo.helloworldk3s;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.GitProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${HOSTNAME:unknown}")
    private String podName;

    @Autowired
    private GitProperties gitProperties;

    int requests = 0;

    @GetMapping("/hello")
    public String hello() {
        String commitId = gitProperties.getCommitId();
        String branch = gitProperties.getBranch();

        requests += 1;
        System.out.println("/test 0.1.0 from " + podName + " - " + commitId + " - " + branch);
        return "version " + commitId + " - pod " + podName + " ==> commitID " + commitId + " - " + branch;
    }

}

