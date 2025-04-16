package com.xavelo.template.api.adapter.in.http.ping;

public record PingResponse (String podName, String commitId, String commitTime) {}

