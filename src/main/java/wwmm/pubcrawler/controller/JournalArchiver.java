package wwmm.pubcrawler.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.v2.inject.Journals;

import javax.inject.Inject;

/**
 * @author Sam Adams
 */
public class JournalArchiver implements Archiver<Journal> {

    private final DBCollection collection;

    @Inject
    public JournalArchiver(@Journals final DBCollection collection) {
        this.collection = collection;
        this.collection.ensureIndex(new BasicDBObject("id", 1), "id_index", true);
    }

    @Override
    public void archive(final Journal journal) {
        final String id = journal.getId().getValue();
        DBObject dbObject = collection.findOne(new BasicDBObject("id", id));
        if (dbObject == null) {
            save(journal);
        } else {
            update(dbObject, journal);
        }
    }

    private void save(final Journal journal) {
        final DBObject dbObject = new BasicDBObject();

        dbObject.put("id", journal.getId().getValue());

        if (journal.getPublisherRef() != null) {
            dbObject.put("publisherRef", journal.getPublisherRef());
        }

        dbObject.put("journalTitle", journal.getTitle());

        if (journal.getUrl() != null) {
            dbObject.put("url", journal.getUrl().toString());
        }

        collection.save(dbObject);
    }

    private void update(final DBObject dbObject, final Journal issue) {
        // TODO update stored issue
    }
    
}
