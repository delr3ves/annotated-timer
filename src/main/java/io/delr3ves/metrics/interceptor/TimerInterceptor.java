package io.delr3ves.metrics.interceptor;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Timed;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * Intercepts methods in order to measure the time they take to execute.
 *
 * @author Sergio Arroyo - @delr3ves
 */
public class TimerInterceptor implements MethodInterceptor {

    public static final String TIMER_SUFIX = "Timer";
    private MetricRegistry metricRegistry;

    public TimerInterceptor(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    /**
     * Intercepts the method and measure the execution time.
     *
     * @param methodInvocation the invocation
     * @return the invocation result.
     * @throws Throwable if the invocation fail
     */
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Timer timer = getTimer(methodInvocation.getMethod());
        Timer.Context ctx = timer.time();
        try {
            return methodInvocation.proceed();
        } finally {
            ctx.stop();
        }
    }

    /**
     * Find the timer in the registry and create it if needed. Then return it.
     *
     * @param method contains the information to find the timer
     * @return the timer.
     */
    private Timer getTimer(Method method) {
        String name = getNameForTimer(method);
        Timer timer = metricRegistry.getTimers().get(name);
        if (timer == null) {
            timer = metricRegistry.timer(name);
        }
        return timer;
    }

    /**
     * Build the name of the metric based on the method and its annotations.
     * @param method the method to be measured.
     * @return the name of the timer.
     */
    private String getNameForTimer(Method method) {
        Timed timed = method.getAnnotation(Timed.class);
        if (timed.absolute()) {
            return timed.name();
        }
        if (timed.name().isEmpty()) {
            return name(method.getDeclaringClass(), method.getName(), TIMER_SUFIX);
        }
        return name(method.getDeclaringClass(), timed.name(), TIMER_SUFIX);
    }

}
