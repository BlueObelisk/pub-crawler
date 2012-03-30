package wwmm.pubcrawler.v2.repositories.mongo;

import com.mongodb.*;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.model.Issue;
import wwmm.pubcrawler.model.Reference;
import wwmm.pubcrawler.v2.inject.Articles;
import wwmm.pubcrawler.v2.repositories.ArticleRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
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
                results.add(mapArticle(cursor.next()));
            }
        } finally {
            cursor.close();
        }
        return results;
    }

    private Article mapArticle(final DBObject dbObject) {
        final Article article = new Article();
        article.setTitle((String) dbObject.get("title"));
        article.setAuthors((List<String>) dbObject.get("authors"));
        final Reference reference = new Reference();
        reference.setVolume((String) article.get("volume"));
        reference.setNumber((String) article.get("number"));
        reference.setPages((String) article.get("pages"));
        article.setReference(reference);
        return article;
    }
}
