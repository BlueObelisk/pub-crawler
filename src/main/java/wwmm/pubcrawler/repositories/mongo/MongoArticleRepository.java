package wwmm.pubcrawler.repositories.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.types.Doi;
import wwmm.pubcrawler.inject.Articles;
import wwmm.pubcrawler.repositories.ArticleRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
@Singleton
public class MongoArticleRepository implements ArticleRepository {

    private final DBCollection collection;

    @Inject
    public MongoArticleRepository(@Articles final DBCollection collection) {
        this.collection = collection;
        this.collection.ensureIndex(new BasicDBObject("id", 1), "id_index", true);
    }

    @Override
    public void updateArticle(final Article article) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Article> getArticlesForIssue(final String issueId) {
        final List<Article> results = new ArrayList<Article>();
        final DBCursor cursor = collection.find(new BasicDBObject("issueRef", issueId));
        try {
            while (cursor.hasNext()) {
                results.add(mapBsonToArticle(cursor.next()));
            }
        } finally {
            cursor.close();
        }
        return results;
    }

    @Override
    public void saveOrUpdateArticle(final Article article) {
        final DBObject dbObject = collection.findOne(new BasicDBObject("id", article.getId().getUid()));
        if (dbObject == null) {
            saveArticle(article);
        } else {
            updateArticle(dbObject, article);
        }
    }

    private void saveArticle(final Article article) {
        final DBObject dbObject = mapArticleToBson(article);

        collection.save(dbObject);
    }

    private void updateArticle(final DBObject dbObject, final Article article) {
        // TODO update stored article
    }

    private DBObject mapArticleToBson(final Article article) {
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

    private Article mapBsonToArticle(final DBObject dbObject) {
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
