package io.delr3ves.metrics.dummy;

import com.codahale.metrics.annotation.Timed;

/**
 * @author Sergio Arroyo - @delr3ves
 */
public class TimedDummyMethod {

    public static final String NAMED_METHOD = "namedMethod";

    @Timed
    public void dummyTimed() {

    }

    @Timed(name= NAMED_METHOD)
    public void dummyNamedTimed() {

    }

    @Timed(name=NAMED_METHOD, absolute = true)
    public void dummyAbsoluteNamedTimed() {

    }

}
