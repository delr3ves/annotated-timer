package io.delr3ves.metrics.interceptor;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.delr3ves.metrics.AnnotatedTimerGuiceModule;
import io.delr3ves.metrics.dummy.NonInterceptedDummyClass;
import io.delr3ves.metrics.dummy.TimedDummyMethod;
import io.delr3ves.metrics.dummy.nonintercepted.NonInterceptedDummyPackage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Sergio Arroyo - @delr3ves
 */
public class TimeInterceptorTest {

    private MetricRegistry metricRegistry;
    private Injector injector;

    @Before
    public void setUp() throws Exception {
        List<Class> excludedClasses = new ArrayList();
        excludedClasses.add(NonInterceptedDummyClass.class);
        metricRegistry = new MetricRegistry();
        injector = Guice.createInjector(new AnnotatedTimerGuiceModule(metricRegistry, excludedClasses,
                Arrays.asList("io.delr3ves.metrics.dummy.nonintercepted")));
    }

    @Test
    public void whenExecuteTimedMethodShouldCreateTheMetric() {
        injector.getInstance(TimedDummyMethod.class).dummyTimed();
        Assert.assertTrue((metricRegistry.getTimers().get(
                "io.delr3ves.metrics.dummy.TimedDummyMethod.dummyTimed.Timer") != null));
    }

    @Test
    public void whenExecuteNamedTimedMethodShouldCreateTheMetric() {
        injector.getInstance(TimedDummyMethod.class).dummyNamedTimed();
        Assert.assertTrue((metricRegistry.getTimers().get(
                "io.delr3ves.metrics.dummy.TimedDummyMethod." + TimedDummyMethod.NAMED_METHOD + ".Timer") != null));
    }

    @Test
    public void whenExecuteAbsoluteNamedTimedMethodShouldCreateTheMetric() {
        injector.getInstance(TimedDummyMethod.class).dummyAbsoluteNamedTimed();
        Assert.assertTrue((metricRegistry.getTimers().get(TimedDummyMethod.NAMED_METHOD) != null));
    }

    @Test
    public void whenExecuteNonInterceptedClassShouldNotCreateTheMetric() {
        injector.getInstance(NonInterceptedDummyClass.class).dummyMethod();
        Assert.assertTrue((metricRegistry.getTimers().get(
                "io.delr3ves.metrics.dummy.NonInterceptedDummyClass.dummyMethod.Timer") == null));
    }

    @Test
    public void whenExecuteNonInterceptedPackageShouldNotCreateTheMetric() {
        injector.getInstance(NonInterceptedDummyPackage.class).dummyMethod();
        Assert.assertTrue((metricRegistry.getTimers().get(
                "io.delr3ves.metrics.dummy.nonintercepted.NonInterceptedDummyPackage.dummyMethod.Timer") == null));
    }

}
