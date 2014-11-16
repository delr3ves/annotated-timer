package io.delr3ves.metrics.dummy;

import com.codahale.metrics.annotation.Timed;

import java.io.Serializable;

/**
 * @author Sergio Arroyo - @delr3ves
 */
public class NonInterceptedDummyClass implements Serializable{

    @Timed
    public void dummyMethod() {

    }
}
