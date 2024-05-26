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

    @GetMapping("/hello")
    public Hello hello() {
        String commitId = gitProperties.getCommitId();
        String branch = gitProperties.getBranch();
        System.out.println(commitId + "-" + podName);
        return new Hello("hello from pod " + podName, commitId);
    }

}

