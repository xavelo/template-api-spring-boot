package com.xavelo.template.metrics;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Identifies a component as an adapter and provides descriptive metadata for metrics.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Adapter {

    String name();

    AdapterMetrics.Type type();

    AdapterMetrics.Direction direction();
}
