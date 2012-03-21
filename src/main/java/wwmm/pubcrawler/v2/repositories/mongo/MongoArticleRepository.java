package wwmm.pubcrawler.v2.repositories.mongo;

import com.mongodb.DBCollection;
import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.v2.inject.Articles;
import wwmm.pubcrawler.v2.repositories.ArticleRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class MongoArticleRepository implements ArticleRepository {

    private final DBCollection collecton;

    @Inject
    public MongoArticleRepository(@Articles final DBCollection collecton) {
        this.collecton = collecton;
    }

    @Override
    public void updateArticle(final Article article) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
