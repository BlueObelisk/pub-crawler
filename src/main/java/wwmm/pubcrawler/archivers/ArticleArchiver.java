package wwmm.pubcrawler.archivers;

import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.repositories.ArticleRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class ArticleArchiver implements Archiver<Article> {
    
    private final ArticleRepository repository;

    @Inject
    public ArticleArchiver(final ArticleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void archive(final Article article) {
        repository.saveOrUpdateArticle(article);
    }

}
