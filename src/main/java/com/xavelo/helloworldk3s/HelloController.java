package com.xavelo.helloworldk3s;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.GitProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private static final Logger logger = LogManager.getLogger(HelloController.class);

    @Value("${HOSTNAME:unknown}")
    private String podName;

    @Autowired
    private GitProperties gitProperties;

    @GetMapping("/hello")
    public Hello hello() {
        String commitId = gitProperties.getCommitId();
        String branch = gitProperties.getBranch();
        String commitTime = gitProperties.getCommitTime().toString();
        logger.info(commitId + "-" + "-" + commitTime + "-" + podName);
        return new Hello("hello from pod " + podName, commitId + " - " + commitTime);
    }

}

