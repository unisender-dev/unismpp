package com.smpp.transport.util;

import org.apache.commons.lang3.time.StopWatch;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * Created by v.maksymenko on 1/13/14.
 * High resolution extension of Apache StopWatch timer
 * with custom toString format
 */
public class StopWatchHires extends StopWatch {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("mm:ss.SSS");
    private static final DecimalFormat DF = new DecimalFormat("000");

    public String toString() {
        return SDF.format( getTime() );
    }

    public String toHiresString() {
        long microSeconds = getNanoTime() / 1000;
        return SDF.format( getTime() ) + DF.format( microSeconds % 1000 );
    }

}
