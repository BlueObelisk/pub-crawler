package wwmm.pubcrawler.v2.repositories.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.JournalId;
import wwmm.pubcrawler.model.id.PublisherId;
import wwmm.pubcrawler.v2.inject.Journals;
import wwmm.pubcrawler.v2.repositories.JournalRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
@Singleton
public class MongoJournalRepository implements JournalRepository {

    private final DBCollection collection;

    @Inject
    public MongoJournalRepository(@Journals final DBCollection collection) {
        this.collection = collection;
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
                results.add(mapJournal(cursor.next()));
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

    private Journal mapJournal(final DBObject dbObject) {
        final Journal journal = new Journal();
        journal.setId(new JournalId((String) dbObject.get("id")));
        journal.setTitle((String) dbObject.get("journalTitle"));
        if (dbObject.containsField("url")) {
            journal.setUrl(URI.create((String) dbObject.get("url")));
        }
        return journal;
    }
}
