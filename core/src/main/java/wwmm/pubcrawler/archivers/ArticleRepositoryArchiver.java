package wwmm.pubcrawler.archivers;

import wwmm.pubcrawler.model.Article;
import wwmm.pubcrawler.repositories.ArticleRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author Sam Adams
 */
@Singleton
public class ArticleRepositoryArchiver implements ArticleArchiver {
    
    private final ArticleRepository repository;

    @Inject
    public ArticleRepositoryArchiver(final ArticleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void archive(final Article article) {
        repository.saveOrUpdateArticle(article);
    }

}
