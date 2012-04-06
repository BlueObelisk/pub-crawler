package wwmm.pubcrawler.repositories.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.inject.Issues;
import wwmm.pubcrawler.repositories.IssueRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
@Singleton
public class MongoIssueRepository implements IssueRepository {

    private final DBCollection collection;

    @Inject
    public MongoIssueRepository(@Issues final DBCollection collection) {
        this.collection = collection;
    }

    @Override
    public Issue getIssue(final String publisher, final String journal, final String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Issue> getIssuesForJournal(final String journalId) {
        final List<Issue> results = new ArrayList<Issue>();
        final DBCursor cursor = collection.find(new BasicDBObject("journalRef", journalId));
        try {
            while (cursor.hasNext()) {
                results.add(mapBsonToIssue(cursor.next()));
            }
        } finally {
            cursor.close();
        }
        return results;
    }

    @Override
    public void addArticles(final Issue issue, final String... issues) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateIssue(final Issue issue) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void saveOrUpdateIssue(final Issue issue) {

        final DBObject dbObject = collection.findOne(new BasicDBObject("id", issue.getId().getUid()));
        if (dbObject == null) {
            save(issue);
        } else {
            update(dbObject, issue);
        }
    }

    private void save(final Issue issue) {
        final DBObject dbObject = mapIssueToBson(issue);

        collection.save(dbObject);
    }

    private void update(final DBObject dbObject, final Issue issue) {
        // TODO update stored issue
    }

    private DBObject mapIssueToBson(final Issue issue) {
        final DBObject dbObject = new BasicDBObject();

        dbObject.put("id", issue.getId().getUid());

        if (issue.getJournalRef() != null) {
            dbObject.put("journalRef", issue.getJournalRef());
        }

        dbObject.put("journalTitle", issue.getJournalTitle());
        dbObject.put("year", issue.getYear());
        dbObject.put("volume", issue.getVolume());
        dbObject.put("number", issue.getNumber());

        if (issue.getUrl() != null) {
            dbObject.put("url", issue.getUrl().toString());
        }

        if (issue.getPreviousIssueId() != null) {
            dbObject.put("prev", issue.getPreviousIssueId());
        }
        if (issue.getNextIssueId() != null) {
            dbObject.put("next", issue.getNextIssueId());
        }
        return dbObject;
    }

    private Issue mapBsonToIssue(final DBObject dbObject) {
        final Issue issue = new Issue();
        issue.setId(new IssueId((String) dbObject.get("id")));
        issue.setVolume((String) dbObject.get("volume"));
        issue.setNumber((String) dbObject.get("number"));
        issue.setYear((String) dbObject.get("year"));
        if (dbObject.containsField("url")) {
            issue.setUrl(URI.create((String) dbObject.get("url")));
        }
        return issue;
    }
}
