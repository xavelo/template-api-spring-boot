package com.xavelo.helloworldk3s;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${HOSTNAME:unknown}")
    private String podName;

    @Value("${git.commit.id}")
    private String gitCommitId;

    int requests = 0;

    @GetMapping("/hello")
    public String hello() {
        String commitId = "ddd";
        String branch = "master";

        requests += 1;
        System.out.println("/test 0.1.0 from " + podName + " - " + commitId);
        return "version " + commitId + " - pod " + podName + " ==> commitID " + commitId;
    }

}

