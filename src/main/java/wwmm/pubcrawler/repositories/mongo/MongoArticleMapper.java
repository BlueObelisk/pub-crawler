package wwmm.pubcrawler.repositories.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.types.Doi;

import java.util.List;

public class MongoArticleMapper {
    public MongoArticleMapper() {
    }

    public DBObject mapArticleToBson(final Article article) {
        final DBObject dbObject = new BasicDBObject();

        dbObject.put("id", article.getId().getUid());

        if (article.getIssueRef() != null) {
            dbObject.put("issueRef", article.getIssueRef().getUid());
        }

        dbObject.put("title", article.getTitle());
        dbObject.put("authors", article.getAuthors());

        if (article.getReference() != null) {
            dbObject.put("journal", article.getReference().getJournalTitle());
            dbObject.put("year", article.getReference().getYear());
            dbObject.put("volume", article.getReference().getVolume());
            dbObject.put("number", article.getReference().getNumber());
            dbObject.put("pages", article.getReference().getPages());
        }

        if (article.getUrl() != null) {
            dbObject.put("url", article.getUrl().toString());
        }
        if (article.getDoi() != null) {
            dbObject.put("doi", article.getDoi().getValue());
        }
        return dbObject;
    }

    public Article mapBsonToArticle(final DBObject dbObject) {
        final Article article = new Article();
        article.setTitle((String) dbObject.get("title"));
        article.setAuthors((List<String>) dbObject.get("authors"));
        final Reference reference = new Reference();
        reference.setJournalTitle((String) dbObject.get("journal"));
        reference.setVolume((String) dbObject.get("volume"));
        reference.setNumber((String) dbObject.get("number"));
        reference.setPages((String) dbObject.get("pages"));
        article.setReference(reference);
        if (dbObject.containsField("doi")) {
            article.setDoi(new Doi((String) dbObject.get("doi")));
        }
        article.setReference(reference);
        if (dbObject.containsField("url")) {
            article.setUrl(URI.create((String) dbObject.get("url")));
        }
        return article;
    }
}