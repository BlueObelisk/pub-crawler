package wwmm.pubcrawler.tasks.repository;

import com.mongodb.BasicDBObject;
import org.joda.time.Duration;
import wwmm.pubcrawler.tasks.DataSink;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class BasicDbObjectDataSink implements DataSink {

    private final BasicDBObject o;

    public BasicDbObjectDataSink(final BasicDBObject o) {
        this.o = o;
    }

    @Override
    public void writeString(final String key, final String value) {
        o.put(key, value);
    }

    @Override
    public void writeLong(final String key, final long value) {
        o.put(key, value);
    }

    @Override
    public void writeInt(final String key, final int value) {
        o.put(key, value);
    }

    @Override
    public void writeUri(final String key, final URI value) {
        writeString(key, value.toString());
    }

    @Override
    public void writeDuration(final String key, final Duration value) {
        writeString(key, value.toString());
    }
}
