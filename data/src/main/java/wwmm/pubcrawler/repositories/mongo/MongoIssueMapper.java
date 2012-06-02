package wwmm.pubcrawler.repositories.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.id.IssueId;

import java.net.URI;

public class MongoIssueMapper {
    public MongoIssueMapper() {
    }

    public DBObject mapIssueToBson(final Issue issue) {
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

    public Issue mapBsonToIssue(final DBObject dbObject) {
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