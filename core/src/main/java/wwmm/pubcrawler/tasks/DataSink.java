package wwmm.pubcrawler.tasks;

import org.joda.time.Duration;

import java.net.URI;

/**
 * @author Sam Adams
 */
public interface DataSink {

    void writeUri(String key, URI value);

    void writeString(String key, String value);

    void writeLong(String key, long value);

    void writeInt(String key, int value);

    void writeDuration(String key, Duration value);
}
