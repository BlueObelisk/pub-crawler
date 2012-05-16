package wwmm.pubcrawler.controller;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Sam Adams
 */
public class TimeUtilsTest {

    @Test
    public void testMillisToNanos() {
        assertEquals(10000000, TimeUtils.millisToNanos(10));
    }

}
