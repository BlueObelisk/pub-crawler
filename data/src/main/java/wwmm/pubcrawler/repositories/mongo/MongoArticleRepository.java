package wwmm.pubcrawler.repositories.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.repositories.ArticleRepository;
import wwmm.pubcrawler.repositories.Articles;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sam Adams
 */
@Singleton
public class MongoArticleRepository extends AbstractMongoRepository implements ArticleRepository {

    private final MongoArticleMapper mongoArticleMapper;

    @Inject
    public MongoArticleRepository(@Articles final DBCollection collection, final MongoArticleMapper mongoArticleMapper) {
        super(collection);
        this.mongoArticleMapper = mongoArticleMapper;
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
                results.add(mongoArticleMapper.mapBsonToArticle(cursor.next()));
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
        final DBObject dbObject = mongoArticleMapper.mapArticleToBson(article);

        collection.save(dbObject);
    }

    private void updateArticle(final DBObject dbObject, final Article article) {
    }
}
