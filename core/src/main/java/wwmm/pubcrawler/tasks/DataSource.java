package wwmm.pubcrawler.tasks;

import org.joda.time.Duration;

import java.net.URI;

/**
 * @author Sam Adams
 */
public interface DataSource {

    URI readUri(String key);

    String readString(String key);

    long readLong(String key);

    int readInt(String key);

    Duration readDuration(String key);
}
