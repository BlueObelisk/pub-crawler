package wwmm.pubcrawler.repositories.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.repositories.JournalRepository;
import wwmm.pubcrawler.repositories.Journals;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
@Singleton
public class MongoJournalRepository extends AbstractMongoRepository implements JournalRepository {

    private final MongoJournalMapper mongoJournalMapper;

    @Inject
    public MongoJournalRepository(@Journals final DBCollection collection, final MongoJournalMapper mongoJournalMapper) {
        super(collection);
        this.mongoJournalMapper = mongoJournalMapper;
        this.collection.ensureIndex(new BasicDBObject("id", 1), "id_index", true);
    }

    @Override
    public Journal getJournal(final PublisherId publisher, final String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Journal> getJournalsForPublisher(final String publisher) {
        final List<Journal> results = new ArrayList<Journal>();
        final DBCursor cursor = collection.find(new BasicDBObject("publisherRef", publisher));
        try {
            while (cursor.hasNext()) {
                results.add(mongoJournalMapper.mapBsonToJournal(cursor.next()));
            }
        } finally {
            cursor.close();
        }
        return results;
    }

    @Override
    public void addIssues(final Journal journal, final String... issues) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateJournal(final PublisherId acs, final Journal journal) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveOrUpdateJournal(final Journal journal) {
        final String id = journal.getId().getUid();
        final DBObject dbObject = collection.findOne(new BasicDBObject("id", id));
        if (dbObject == null) {
            save(journal);
        } else {
            update(dbObject, journal);
        }
    }

    private void save(final Journal journal) {
        final DBObject dbObject = mongoJournalMapper.mapJournalToBson(journal);

        collection.save(dbObject);
    }

    private void update(final DBObject dbObject, final Journal issue) {
    }
}
