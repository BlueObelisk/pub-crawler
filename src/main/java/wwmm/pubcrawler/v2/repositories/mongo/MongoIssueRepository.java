package wwmm.pubcrawler.v2.repositories.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.IssueId;
import wwmm.pubcrawler.v2.inject.Issues;
import wwmm.pubcrawler.v2.repositories.IssueRepository;

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
                results.add(mapIssue(cursor.next()));
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

    private Issue mapIssue(final DBObject dbObject) {
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
