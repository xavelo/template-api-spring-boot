/**
 * Provides annotations and support for instrumenting adapter invocations.
 * <p>
 * Add {@link com.xavelo.template.metrics.EnableAdapterMetrics @EnableAdapterMetrics}
 * to a Spring configuration class and annotate adapter methods with
 * {@link com.xavelo.template.metrics.CountAdapterInvocation @CountAdapterInvocation}
 * to automatically emit counters and timers tagged with the adapter name, type,
 * direction, and invocation result.
 */
@NonNullApi
package com.xavelo.template.metrics;

import org.springframework.lang.NonNullApi;
