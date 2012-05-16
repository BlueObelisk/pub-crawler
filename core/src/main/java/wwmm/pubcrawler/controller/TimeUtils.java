package wwmm.pubcrawler.controller;

import java.util.concurrent.TimeUnit;

public class TimeUtils {

    public static long millisToNanos(final long millis) {
        return TimeUnit.MILLISECONDS.toNanos(millis);
    }

}