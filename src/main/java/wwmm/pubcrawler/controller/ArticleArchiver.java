package wwmm.pubcrawler.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.v2.inject.Articles;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class ArticleArchiver implements Archiver<Article> {
    
    private final DBCollection collection;

    @Inject
    public ArticleArchiver(@Articles final DBCollection collection) {
        this.collection = collection;
        this.collection.ensureIndex(new BasicDBObject("id", 1), "id_index", true);
    }

    @Override
    public void archive(final Article article) {
        final DBObject dbObject = collection.findOne(new BasicDBObject("id", article.getId().getUid()));
        if (dbObject == null) {
            saveArticle(article);
        } else {
            updateArticle(dbObject, article);
        }
    }

    private void saveArticle(final Article article) {
        final DBObject dbObject = new BasicDBObject();

        dbObject.put("id", article.getId().getUid());

        if (article.getIssueRef() != null) {
            dbObject.put("issueRef", article.getIssueRef());
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

        collection.save(dbObject);
    }

    private void updateArticle(final DBObject dbObject, final Article article) {
        // TODO update stored article
    }

}
