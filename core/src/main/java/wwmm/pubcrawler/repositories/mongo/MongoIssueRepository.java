package wwmm.pubcrawler.repositories.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import wwmm.pubcrawler.inject.Issues;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.repositories.IssueRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
@Singleton
public class MongoIssueRepository implements IssueRepository {

    private final DBCollection collection;
    private final MongoIssueMapper mongoIssueMapper;

    @Inject
    public MongoIssueRepository(@Issues final DBCollection collection, final MongoIssueMapper mongoIssueMapper) {
        this.collection = collection;
        this.mongoIssueMapper = mongoIssueMapper;
        this.collection.ensureIndex(new BasicDBObject("id", 1), "id_index", true);
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
                results.add(mongoIssueMapper.mapBsonToIssue(cursor.next()));
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
        final DBObject dbObject = mongoIssueMapper.mapIssueToBson(issue);

        collection.save(dbObject);
    }

    private void update(final DBObject dbObject, final Issue issue) {
        // TODO update stored issue
    }
}