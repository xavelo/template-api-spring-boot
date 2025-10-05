package com.xavelo.template.application.domain;

/**
 * Simple domain event carrying an identifier and a text payload.
 */
public record Event(String id, String payload) {
}
