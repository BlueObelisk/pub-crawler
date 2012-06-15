package wwmm.pubcrawler.repositories.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * @author Sam Adams
 */
public abstract class AbstractMongoRepository {

    private static final String SEQUENCE = "sequence";

    protected final DBCollection collection;

    protected AbstractMongoRepository(final DBCollection collection) {
        this.collection = collection;
        this.collection.ensureIndex(new BasicDBObject(SEQUENCE, 1));
    }

    public long getNextSequence() {
        final DBCursor cursor =
         collection
          .find(new BasicDBObject(), new BasicDBObject(SEQUENCE, 1))
          .sort(new BasicDBObject(SEQUENCE, -1))
          .limit(1);

        try {
            if (cursor.hasNext()) {
                final DBObject result = cursor.next();
                final long maxSequence = ((Number) result.get(SEQUENCE)).longValue();
                return maxSequence + 1;
            } else {
                return 1L;
            }
        } finally {
            cursor.close();
        }
    }

}
