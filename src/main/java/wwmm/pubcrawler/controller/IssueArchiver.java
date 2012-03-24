package wwmm.pubcrawler.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.v2.inject.Issues;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
public class IssueArchiver implements Archiver<Issue> {

    private final DBCollection collection;

    @Inject
    public IssueArchiver(@Issues final DBCollection collection) {
        this.collection = collection;
        this.collection.ensureIndex(new BasicDBObject("id", 1), "id_index", true);
    }

    @Override
    public void archive(final Issue issue) {
        DBObject dbObject = collection.findOne(new BasicDBObject("id", issue.getId().getValue()));
        if (dbObject == null) {
            save(issue);
        } else {
            update(dbObject, issue);
        }
    }

    private void save(final Issue issue) {
        final DBObject dbObject = new BasicDBObject();

        dbObject.put("id", issue.getId().getValue());

        dbObject.put("journal", issue.getJournalTitle());
        dbObject.put("year", issue.getYear());
        dbObject.put("volume", issue.getVolume());
        dbObject.put("number", issue.getNumber());

        if (issue.getUrl() != null) {
            dbObject.put("url", issue.getUrl().toString());
        }

        List<String> articles = new ArrayList<String>();
        for (Article article : issue.getArticles()) {
            articles.add(article.getId().getValue());
        }

        if (issue.getPreviousIssueId() != null) {
            dbObject.put("prev", issue.getPreviousIssueId());
        }
        if (issue.getNextIssueId() != null) {
            dbObject.put("next", issue.getNextIssueId());
        }

        collection.save(dbObject);
    }

    private void update(final DBObject dbObject, final Issue issue) {
        // TODO update stored issue
    }
    
}
