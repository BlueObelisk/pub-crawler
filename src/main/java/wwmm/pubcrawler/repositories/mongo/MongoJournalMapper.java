package wwmm.pubcrawler.repositories.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import wwmm.pubcrawler.model.Journal;
import wwmm.pubcrawler.model.id.JournalId;

import java.net.URI;

public class MongoJournalMapper {
    public MongoJournalMapper() {
    }

    public DBObject mapJournalToBson(final Journal journal) {
        final DBObject dbObject = new BasicDBObject();

        dbObject.put("id", journal.getId().getUid());

        if (journal.getPublisherRef() != null) {
            dbObject.put("publisherRef", journal.getPublisherRef());
        }

        dbObject.put("journalTitle", journal.getTitle());

        if (journal.getUrl() != null) {
            dbObject.put("url", journal.getUrl().toString());
        }
        return dbObject;
    }

    public Journal mapBsonToJournal(final DBObject dbObject) {
        final Journal journal = new Journal();
        journal.setId(new JournalId((String) dbObject.get("id")));
        journal.setTitle((String) dbObject.get("journalTitle"));
        if (dbObject.containsField("url")) {
            journal.setUrl(URI.create((String) dbObject.get("url")));
        }
        return journal;
    }
}