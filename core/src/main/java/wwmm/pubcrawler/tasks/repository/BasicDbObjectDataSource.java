package wwmm.pubcrawler.tasks.repository;

import com.mongodb.BasicDBObject;
import org.joda.time.Duration;
import wwmm.pubcrawler.tasks.DataSource;

import java.net.URI;

/**
 * @author Sam Adams
 */
public class BasicDbObjectDataSource implements DataSource {

    private final BasicDBObject o;

    public BasicDbObjectDataSource(final BasicDBObject o) {
        this.o = o;
    }

    @Override
    public URI readUri(final String key) {
        return URI.create(readString(key));
    }

    @Override
    public String readString(final String key) {
        return o.getString(key);
    }

    @Override
    public long readLong(final String key) {
        return o.getLong(key);
    }

    @Override
    public int readInt(final String key) {
        return o.getInt(key);
    }

    @Override
    public Duration readDuration(final String key) {
        return new Duration(readString(key));
    }
}
