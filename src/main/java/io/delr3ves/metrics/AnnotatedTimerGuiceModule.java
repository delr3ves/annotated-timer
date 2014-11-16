package io.delr3ves.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import io.delr3ves.metrics.interceptor.TimerInterceptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Module to configure timer interceptors.
 * 
 * @author Sergio Arroyo - @delr3ves
 */
public class AnnotatedTimerGuiceModule extends AbstractModule {

    private MetricRegistry metricRegistry;
    private List<Class> nonInterceptedClassesexcludedClasses;
    private List<String> nonInterceptedPackages;

    /**
     * Creates a module which intercept all classes.
     *
     * @param metricRegistry the registry to store the timers
     */
    public AnnotatedTimerGuiceModule(MetricRegistry metricRegistry) {
        this(metricRegistry, new ArrayList<Class>(), new ArrayList<String>());
    }

    /**
     * Creates a module which avoid some classes or packages.
     *
     * @param metricRegistry the registry to store the timers
     * @param nonInterceptedClasses the specific classes (and subclasess) which won't be measured
     * @param nonInterceptedPackages the packages (and subpackages) which won't be measured
     */
    public AnnotatedTimerGuiceModule(MetricRegistry metricRegistry, List<Class> nonInterceptedClasses,
                                     List<String> nonInterceptedPackages) {
        this.metricRegistry = metricRegistry;
        this.nonInterceptedClassesexcludedClasses = nonInterceptedClasses;
        this.nonInterceptedPackages = nonInterceptedPackages;
    }

    /**
     * Prepare Guice to intercept methods annotated with Timed.
     */
    @Override
    protected void configure() {
        Matcher matcher = buildMatcherForTimer();
        bindInterceptor(matcher, Matchers.annotatedWith(Timed.class),
                new TimerInterceptor(metricRegistry));
    }

    private Matcher buildMatcherForTimer() {
        Matcher matcher = Matchers.any();
        if (nonInterceptedClassesexcludedClasses != null && !nonInterceptedClassesexcludedClasses.isEmpty()) {
            for (Class clazz : nonInterceptedClassesexcludedClasses) {
                matcher = matcher.and(Matchers.not(Matchers.subclassesOf(clazz)));
            }
        }
        if (nonInterceptedClassesexcludedClasses != null && !nonInterceptedClassesexcludedClasses.isEmpty()) {
            for (String pack : nonInterceptedPackages) {
                matcher = matcher.and(Matchers.not(Matchers.inSubpackage(pack)));
            }
        }
        return matcher;
    }
}
