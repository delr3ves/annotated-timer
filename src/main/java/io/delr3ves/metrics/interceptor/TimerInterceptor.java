package io.delr3ves.metrics.interceptor;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Timed;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * @author Sergio Arroyo - @delr3ves
 */
public class TimerInterceptor implements MethodInterceptor {

    public static final String TIMER_SUFIX = "Timer";
    private MetricRegistry metricRegistry;

    public TimerInterceptor(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Timer timer = getTimer(methodInvocation);
        Timer.Context ctx = timer.time();
        try {
            return methodInvocation.proceed();
        } finally {
            ctx.stop();
        }
    }

    private Timer getTimer(MethodInvocation methodInvocation) {
        String name = getNameForTimer(methodInvocation.getMethod());
        Timer timer = metricRegistry.getTimers().get(name);
        if (timer == null) {
            timer = metricRegistry.timer(name);
        }
        return timer;
    }

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
