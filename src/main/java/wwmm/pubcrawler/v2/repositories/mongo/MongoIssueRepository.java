package wwmm.pubcrawler.v2.repositories.mongo;

import com.mongodb.DBCollection;
import wwmm.pubcrawler.v2.inject.Issues;
import wwmm.pubcrawler.v2.repositories.IssueRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
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
    public wwmm.pubcrawler.model.Issue getIssue(final String publisher, final String journal, final String id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<wwmm.pubcrawler.model.Issue> getIssuesForJournal(final String publisher, final String journal) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void addArticles(final wwmm.pubcrawler.model.Issue issue, final String... issues) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateIssue(final wwmm.pubcrawler.model.Issue issue) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
