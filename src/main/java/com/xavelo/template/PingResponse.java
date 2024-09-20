package com.xavelo.template;

import lombok.Getter;

@Getter
public class PingResponse {
    private String podName;
    private String commitId;
    private String commitTime;

    public PingResponse(String podName, String commitId, String commitTime) {
        this.podName = podName;
        this.commitId = commitId;
        this.commitTime = commitTime;
    }
}
